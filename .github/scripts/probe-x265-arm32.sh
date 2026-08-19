#!/bin/sh
#
# Finds an x265 configuration that both compiles and statically links on 32 bit arm.
#
# On armv7 x265 fails two different ways. With its assembly on it will not compile.
# With -DENABLE_ASSEMBLY=OFF it compiles, but the C code still calls
# x265_cpu_fast_neon_mrc_test, which only the assembly defines, so a static link
# fails with an undefined reference. This tries the plausible ways round that and
# says which, if any, gets all the way through.
#
# Meant to be run on real 32 bit arm hardware, where each attempt takes minutes
# rather than the hour and a half the same thing costs under emulation in CI:
#
#   docker run --rm --platform linux/arm/v7 \
#     -v "$PWD/.github/scripts/probe-x265-arm32.sh:/probe.sh:ro" \
#     alpine:3.21 sh /probe.sh
#
# Check first that this is running natively and not under emulation, or there is
# nothing gained:
#
#   docker run --rm --platform linux/arm/v7 alpine:3.21 uname -m     # expect armv7l
#
# The last line of output is the answer. Send back the whole thing either way, the
# failures say as much as the successes.

set -eu

X265_VERSION=4.1
SRC=/tmp/x265src
OUT=/tmp/x265out

echo "### machine: $(uname -m), $(nproc) cpus"
echo "### if that does not say armv7l, this is testing the wrong architecture"
echo

apk add --no-cache build-base cmake git curl tar xz pkgconf bash diffutils >/dev/null 2>&1
echo "### gcc: $(gcc --version | head -1)"
echo

mkdir -p "$SRC" && cd "$SRC"
curl -fSL --retry 3 -o x265.tar.gz \
  "https://bitbucket.org/multicoreware/x265_git/downloads/x265_${X265_VERSION}.tar.gz"
tar -xf x265.tar.gz

cat > /tmp/uses_x265.c <<'EOF'
#include <x265.h>
int main(void) { return (int) (long) x265_api_get; }
EOF

winner=""

try() {
  name="$1"
  shift
  echo "=== $name ==="
  echo "    cmake $*"

  rm -rf "/tmp/build-$name" "$OUT/$name"
  mkdir -p "/tmp/build-$name"
  cd "/tmp/build-$name"

  if ! cmake -G "Unix Makefiles" \
        -DCMAKE_INSTALL_PREFIX="$OUT/$name" \
        -DENABLE_SHARED=OFF \
        -DENABLE_CLI=OFF \
        -DCMAKE_BUILD_TYPE=Release \
        "$@" \
        "$SRC/x265_${X265_VERSION}/source" > cmake.log 2>&1; then
    echo "    cmake failed: $(grep -i -m1 error cmake.log || echo 'see cmake.log')"
    return 0
  fi

  if ! make -j"$(nproc)" > make.log 2>&1; then
    echo "    compile failed: $(grep -i -m1 'error' make.log || echo 'see make.log')"
    return 0
  fi

  make install > install.log 2>&1
  echo "    compiled"

  # The part that actually matters. A shared link hides the missing symbols, a
  # static one is what ffmpeg does here and is where it has been breaking.
  if gcc -I"$OUT/$name/include" /tmp/uses_x265.c \
       -L"$OUT/$name/lib" -lx265 -lstdc++ -lm -static \
       -o /tmp/uses_x265 > link.log 2>&1; then
    echo "    STATIC LINK OK  <-- usable"
    [ -n "$winner" ] || winner="$name"
  else
    echo "    static link failed: $(grep -m1 'undefined reference' link.log || grep -m1 -i error link.log || echo 'see link.log')"
  fi
}

# Plain, the way it is built on every other architecture
try plain

# No assembly at all. Compiles, and is where the undefined reference shows up.
try no-asm -DENABLE_ASSEMBLY=OFF

# No assembly and no neon, in case the reference comes in through the neon path
try no-asm-no-neon -DENABLE_ASSEMBLY=OFF -DENABLE_NEON=OFF

# Assembly on, but tell gcc which fpu it has. armv7 refuses to inline the neon
# intrinsics unless it is told, which is what breaks aom the same way.
try asm-with-neon-flags \
  -DCMAKE_C_FLAGS="-mfpu=neon -mfloat-abi=hard" \
  -DCMAKE_CXX_FLAGS="-mfpu=neon -mfloat-abi=hard"

# Same, without assembly, in case the flags alone settle the intrinsics
try no-asm-with-neon-flags -DENABLE_ASSEMBLY=OFF \
  -DCMAKE_C_FLAGS="-mfpu=neon -mfloat-abi=hard" \
  -DCMAKE_CXX_FLAGS="-mfpu=neon -mfloat-abi=hard"

echo
echo "############################################################"
if [ -n "$winner" ]; then
  echo "USE: $winner"
else
  echo "USE: none of these worked, x265 stays out of the 32 bit arm build"
fi
echo "############################################################"
