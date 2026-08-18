#!/bin/sh
#
# Builds a fully static ffmpeg inside alpine.
#
# Alpine has no static archive for most of the codec libraries this project needs,
# so they are built here from source with shared libraries turned off. What Alpine
# does ship statically (x264, lame, vorbis, theora, freetype, fribidi, harfbuzz,
# webp, soxr, openssl, zlib) is taken from packages, there is nothing to gain from
# compiling those again.
#
# Run inside the container, not on the host:
#   docker run --rm -v "$PWD/out:/out" -v "$PWD/script.sh:/build.sh" alpine sh /build.sh
#
# Expects FFMPEG_VERSION, OUTPUT_NAME, HOST_UID and HOST_GID in the environment.

# No -x. The trace goes to stderr, and some of these configure scripts build their
# config.status by evaluating captured output, so the +cat markers end up being run
# as commands. The echo lines below say where we are instead.
set -eu

PREFIX=/opt/ffbuild
SRC=/tmp/src
mkdir -p "$PREFIX" "$SRC"

export PKG_CONFIG_PATH="$PREFIX/lib/pkgconfig:$PREFIX/lib64/pkgconfig:/usr/lib/pkgconfig"
export CFLAGS="-I$PREFIX/include -O2"
export CXXFLAGS="$CFLAGS"
export LDFLAGS="-L$PREFIX/lib -L$PREFIX/lib64"
JOBS="$(nproc)"

# Several of these configure scripts are written for bash, busybox ash trips over
# them with errors that point nowhere useful
export CONFIG_SHELL=/bin/bash

# Versions are pinned so a build is repeatable and a failure is not a moving target
OPUS_VERSION=1.5.2
VPX_VERSION=1.15.0
X265_VERSION=4.1
ASS_VERSION=0.17.3
XVID_VERSION=1.3.7
OPENCORE_VERSION=0.1.6
VOAMRWBENC_VERSION=0.1.3
OPENJPEG_VERSION=2.5.2
DAV1D_VERSION=1.5.0
AOM_VERSION=3.10.0

ARCH="$(uname -m)"
echo "=== building for ${ARCH} ==="

echo "=== toolchain and the libraries alpine can give us statically ==="
apk add --no-cache \
  build-base coreutils pkgconf cmake meson ninja \
  autoconf automake libtool git tar xz curl perl bash \
  diffutils findutils grep sed \
  zlib-dev zlib-static \
  bzip2-dev bzip2-static \
  xz-dev \
  openssl-dev openssl-libs-static \
  x264-dev \
  lame-dev \
  libogg-dev libogg-static \
  libvorbis-dev libvorbis-static \
  libtheora-dev libtheora-static \
  freetype-dev freetype-static \
  fribidi-dev fribidi-static \
  harfbuzz-dev harfbuzz-static \
  brotli-dev brotli-static \
  expat-dev expat-static \
  graphite2-dev graphite2-static \
  libpng-dev libpng-static \
  libwebp-dev libwebp-static \
  soxr-dev soxr-static \
  fontconfig-dev fontconfig-static \
  glib-dev glib-static \
  gettext-dev gettext-static \
  pcre2-dev \
  libgomp

# nasm and yasm assemble x86, they are neither packaged nor wanted elsewhere
case "$ARCH" in
  x86_64|i686) apk add --no-cache nasm yasm ;;
  *) echo "not x86, skipping the x86 assemblers" ;;
esac

fetch() {
  # fetch <url> <directory-it-unpacks-to>
  cd "$SRC"
  echo "--- fetching $1"
  curl -fSL --retry 3 --retry-delay 5 --max-time 600 -o src.tar.gz "$1"
  rm -rf "$2"
  tar -xf src.tar.gz
  rm -f src.tar.gz
  cd "$2"
}

fetch_git() {
  # fetch_git <repo-url> <tag> <directory>
  # Codeload rate limits archive downloads from CI often enough to break a build,
  # a shallow clone of the tag is steadier and costs about the same
  cd "$SRC"
  echo "--- cloning $1 at $2"
  rm -rf "$3"
  git clone --depth 1 --branch "$2" "$1" "$3"
  cd "$3"
}


require_archive() {
  # require_archive <name.a> <what it was>
  if [ ! -f "$PREFIX/lib/$1" ] && [ ! -f "$PREFIX/lib64/$1" ]; then
    echo "ERROR: $2 finished without installing $1. Look at its build above, not at"
    echo "       the ffmpeg configure further down, which will only say it is missing."
    exit 1
  fi
  echo "    built $1"
}

echo "=== opus ==="
fetch "https://downloads.xiph.org/releases/opus/opus-${OPUS_VERSION}.tar.gz" "opus-${OPUS_VERSION}"
bash ./configure --prefix="$PREFIX" --disable-shared --enable-static --disable-doc --disable-extra-programs
make -j"$JOBS"
make install

require_archive libopus.a "opus"

# speex is deliberately absent. Alpine packages it nowhere, and its configure fails
# inside this container in a way that is not worth chasing for a codec opus replaced
# years ago. Its encoder is the one accepted regression against the 4.4.1 build.

echo "=== libvpx ==="
fetch_git "https://github.com/webmproject/libvpx.git" "v${VPX_VERSION}" "libvpx"
# We build natively, under emulation on 32 bit arm, but libvpx picks a target like
# armv7-linux-gcc and then looks for an arm-linux-gnueabihf- prefixed compiler that
# is not there, reporting only that it cannot link. An empty CROSS points it at the
# plain toolchain it actually has.
export CROSS=
vpx_configure() {
  bash ./configure --prefix="$PREFIX" \
    --disable-shared --enable-static --enable-pic \
    --enable-vp8 --enable-vp9 --enable-vp9-highbitdepth \
    --disable-examples --disable-tools --disable-docs --disable-unit-tests "$@"
}
# generic-gnu drops the hand written assembly, so it is a fallback rather than the
# first choice, but a slower vp9 beats no vp9 at all
vpx_configure || {
  echo "--- libvpx did not take the detected target, falling back to generic-gnu"
  make distclean >/dev/null 2>&1 || true
  vpx_configure --target=generic-gnu
}
make -j"$JOBS"
make install
unset CROSS

require_archive libvpx.a "libvpx"

# x265 is left out on 32 bit arm. With assembly on it does not compile there, and
# with assembly off it still refers to x265_cpu_fast_neon_mrc_test, which only the
# assembly provides, so it does not link either. The 4.4 build this replaces on that
# platform carried no x265 at all, so nothing is lost by matching it.
WANT_X265=yes
case "$ARCH" in
  armv7l|armv7|armhf) WANT_X265=no ;;
esac

if [ "$WANT_X265" = yes ]; then
  echo "=== x265 ==="
  fetch "https://bitbucket.org/multicoreware/x265_git/downloads/x265_${X265_VERSION}.tar.gz" "x265_${X265_VERSION}"
  cd source
  cmake -G "Unix Makefiles" \
    -DCMAKE_INSTALL_PREFIX="$PREFIX" \
    -DENABLE_SHARED=OFF \
    -DENABLE_CLI=OFF \
    -DCMAKE_BUILD_TYPE=Release .
  make -j"$JOBS"
  make install
  # x265 advertises -lgcc_s in its pkg-config file, which does not exist in a static
  # musl link and would make the ffmpeg check fail for the wrong reason
  sed -i 's/-lgcc_s//g' "$PREFIX/lib/pkgconfig/x265.pc" || true
else
  echo "=== x265 skipped on ${ARCH} ==="
fi

X265_CONFIGURE_FLAG=--enable-libx265
if [ "$WANT_X265" = yes ]; then
  require_archive libx265.a "x265"
else
  X265_CONFIGURE_FLAG=
fi

echo "=== libass ==="
fetch_git "https://github.com/libass/libass.git" "${ASS_VERSION}" "libass"
./autogen.sh
bash ./configure --prefix="$PREFIX" --disable-shared --enable-static
make -j"$JOBS"
make install

require_archive libass.a "libass"

echo "=== xvid ==="
fetch "https://downloads.xvid.com/downloads/xvidcore-${XVID_VERSION}.tar.gz" "xvidcore"
cd build/generic
bash ./configure --prefix="$PREFIX" --disable-shared --enable-static
make -j"$JOBS"
make install
rm -f "$PREFIX"/lib/libxvidcore.so*

require_archive libxvidcore.a "xvid"

echo "=== opencore-amr ==="
fetch "https://downloads.sourceforge.net/opencore-amr/opencore-amr-${OPENCORE_VERSION}.tar.gz" "opencore-amr-${OPENCORE_VERSION}"
bash ./configure --prefix="$PREFIX" --disable-shared --enable-static
make -j"$JOBS"
make install

require_archive libopencore-amrnb.a "opencore-amr"

echo "=== vo-amrwbenc ==="
fetch "https://downloads.sourceforge.net/opencore-amr/vo-amrwbenc-${VOAMRWBENC_VERSION}.tar.gz" "vo-amrwbenc-${VOAMRWBENC_VERSION}"
bash ./configure --prefix="$PREFIX" --disable-shared --enable-static
make -j"$JOBS"
make install

require_archive libvo-amrwbenc.a "vo-amrwbenc"

echo "=== openjpeg ==="
fetch_git "https://github.com/uclouvain/openjpeg.git" "v${OPENJPEG_VERSION}" "openjpeg"
cmake -G "Unix Makefiles" \
  -DCMAKE_INSTALL_PREFIX="$PREFIX" \
  -DBUILD_SHARED_LIBS=OFF \
  -DBUILD_CODEC=OFF \
  -DCMAKE_BUILD_TYPE=Release .
make -j"$JOBS"
make install

require_archive libopenjp2.a "openjpeg"

echo "=== dav1d ==="
fetch "https://code.videolan.org/videolan/dav1d/-/archive/${DAV1D_VERSION}/dav1d-${DAV1D_VERSION}.tar.gz" "dav1d-${DAV1D_VERSION}"
meson setup build --prefix="$PREFIX" --libdir=lib --default-library=static --buildtype=release \
  -Denable_tools=false -Denable_tests=false
ninja -C build
ninja -C build install

require_archive libdav1d.a "dav1d"

echo "=== aom ==="
fetch "https://storage.googleapis.com/aom-releases/libaom-${AOM_VERSION}.tar.gz" "libaom-${AOM_VERSION}"
mkdir -p aom_build && cd aom_build
# aom reaches for neon intrinsics on arm, and on 32 bit arm gcc refuses to inline
# them unless the fpu is named, so the build dies on always_inline. Turning the
# assembly off there costs av1 encoding speed on a platform where av1 encoding is
# theoretical anyway, and is the difference between a package and no package.
AOM_NEON=ON
case "$ARCH" in
  armv7l|armv7|armhf) AOM_NEON=OFF ;;
esac
cmake -G "Unix Makefiles" \
  -DCMAKE_INSTALL_PREFIX="$PREFIX" \
  -DBUILD_SHARED_LIBS=OFF \
  -DENABLE_NEON="$AOM_NEON" \
  -DENABLE_TESTS=OFF -DENABLE_EXAMPLES=OFF -DENABLE_DOCS=OFF -DENABLE_TOOLS=OFF \
  -DCMAKE_BUILD_TYPE=Release ..
make -j"$JOBS"
make install

require_archive libaom.a "aom"

echo "=== what we have to link against ==="
ls -1 "$PREFIX"/lib/*.a 2>/dev/null || true
for lib in libx264 libmp3lame libvorbis libtheora libfreetype libwebp libsoxr libssl libz; do
  ls /usr/lib/"${lib}".a >/dev/null 2>&1 \
    && echo "package static ok      ${lib}.a" \
    || echo "package static MISSING ${lib}.a"
done

echo "=== ffmpeg ${FFMPEG_VERSION} ==="
cd "$SRC"
curl -fSL --retry 3 --max-time 900 -o ffmpeg.tar.xz \
  "https://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.xz"
tar -xf ffmpeg.tar.xz
cd "ffmpeg-${FFMPEG_VERSION}"

# --enable-version3 because this project is GPL3. openssl 3 is apache 2.0, which
# that permits. -static in the link flags is what makes the result self contained.
./configure \
  --prefix=/usr/local \
  --pkg-config-flags="--static" \
  --extra-cflags="-I$PREFIX/include" \
  --extra-ldflags="-L$PREFIX/lib -L$PREFIX/lib64 -static" \
  --extra-libs="-lpthread -lm -lgomp -lstdc++" \
  --disable-shared \
  --enable-static \
  --enable-gpl \
  --enable-version3 \
  --enable-openssl \
  --enable-libx264 \
  $X265_CONFIGURE_FLAG \
  --enable-libmp3lame \
  --enable-libopus \
  --enable-libvorbis \
  --enable-libtheora \
  --enable-libvpx \
  --enable-libaom \
  --enable-libdav1d \
  --enable-libxvid \
  --enable-libass \
  --enable-libfreetype \
  --enable-libharfbuzz \
  --enable-libfontconfig \
  --enable-libfribidi \
  --enable-libwebp \
  --enable-libsoxr \
  --enable-libopenjpeg \
  --enable-libopencore-amrnb \
  --enable-libopencore-amrwb \
  --enable-libvo-amrwbenc \
  --disable-doc \
  --disable-debug \
  --disable-ffplay \
  || { echo "=== configure failed, tail of the log ==="; tail -80 ffbuild/config.log; exit 1; }

make -j"$JOBS"
strip ffmpeg

cp ffmpeg "/out/${OUTPUT_NAME}"
# The container runs as root and the runner does not, so hand the file over or
# nothing outside can so much as chmod it
chmod 0755 "/out/${OUTPUT_NAME}"
chown "${HOST_UID}:${HOST_GID}" "/out/${OUTPUT_NAME}"
echo "=== done, $(ls -l "/out/${OUTPUT_NAME}") ==="
