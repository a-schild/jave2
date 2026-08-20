# JAVE2

<!--
  The release and snapshot badges are live, they read Maven Central.
  The ffmpeg badges are not, they are written by hand. Update them here when
  the bundled binaries change, the version each package really carries is
  asserted by BundledFFmpegVersionTest.
-->

| Package | | Release | Snapshot | ffmpeg release | ffmpeg snapshot | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `jave-core` | the library itself, no binary | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-core?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-core) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-core%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-n%2Fa-lightgrey) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-n%2Fa-lightgrey) | Supported |
| `jave-bom` | bill of materials, no binary | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-bom?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-bom) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-bom%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-n%2Fa-lightgrey) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-n%2Fa-lightgrey) | Supported |
| `jave-all-deps` | core plus every platform below | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-all-deps?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-all-deps) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-all-deps%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | Supported |
| `jave-nativebin-win64` | Windows x64 | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-win64?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-win64) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-win64%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | Supported |
| `jave-nativebin-win-arm64` | Windows on ARM | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-win-arm64?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-win-arm64) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-win-arm64%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | Supported |
| `jave-nativebin-linux64` | Linux x64 | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux64?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux64) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux64%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | Supported |
| `jave-nativebin-linux-arm64` | Linux ARM 64 bit | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux-arm64?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux-arm64) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux-arm64%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | Supported |
| `jave-nativebin-linux-arm32` | Linux ARM 32 bit | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux-arm32?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux-arm32) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux-arm32%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | Supported |
| `jave-nativebin-osxm1` | macOS Apple silicon | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-osxm1?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-osxm1) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-osxm1%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.x-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.x-blue) | Supported |
| `jave-nativebin-osx64` | macOS Intel | [![release](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-osx64?label=%20)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-osx64) | ![snapshot](https://img.shields.io/maven-metadata/v?label=%20&color=orange&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-osx64%2Fmaven-metadata.xml) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | ![ffmpeg](https://img.shields.io/badge/ffmpeg-9.0.1-blue) | **Deprecated** |

> One package is deprecated and two were removed in 4.0.0, see
> [supported platforms](#supported-platforms) below.

The JAVE2 (Java Audio Video Encoder) library is Java wrapper on the ffmpeg
project. Developers can take take advantage of JAVE2 to transcode audio 
and video files from a format to another. In example you can transcode
an **AVI** file to a **MPEG** one, you can change a **DivX** video stream into a
(youtube like) **Flash FLV** one, you can convert a **WAV** audio file to a **MP3** or a
**Ogg Vorbis** one, you can separate and transcode audio and video tracks, you can
resize videos, changing their sizes and proportions and so on.

Many other formats, containers and operations are supported by JAVE2.

## Supported platforms

> JAVE requires **Java 8 or higher**

> JAVE can also be easily ported to other OS and hardware configurations,
see the JAVE manual for details.

| Operating system | Architecture | Package | Status |
| --- | --- | --- | --- |
| Windows | x64 | `jave-nativebin-win64` | Supported |
| Windows | ARM 64 bit | `jave-nativebin-win-arm64` | Supported, new in 4.0.0 |
| macOS | Apple silicon | `jave-nativebin-osxm1` | Supported |
| macOS | Intel x64 | `jave-nativebin-osx64` | **Deprecated**, see below |
| Linux | x64 | `jave-nativebin-linux64` | Supported |
| Linux | ARM 64 bit | `jave-nativebin-linux-arm64` | Supported |
| Linux | ARM 32 bit | `jave-nativebin-linux-arm32` | Supported |
| Windows | x86 32 bit | none | **Removed in 4.0.0**, use 3.6.0 |
| Linux | x86 32 bit | none | **Removed in 4.0.0**, use 3.6.0 |

### macOS on intel is deprecated

Apple ends support for intel hardware with macOS 27, so `jave-nativebin-osx64` will be
removed in a later release. It is still built, still published and still part of
`jave-all-deps`, so nothing breaks today. On apple silicon use `jave-nativebin-osxm1`,
which is unaffected.

### The 32 bit x86 packages were removed in 4.0.0

`jave-nativebin-win32` and `jave-nativebin-linux32` are no longer published. ffmpeg
itself stopped publishing builds for 32 bit Windows, so that binary could not be
brought past 4.4.1 by any route, and 32 bit x86 Linux went with it. **Stay on 3.6.0**
if you need either. 32 bit ARM is not affected and remains supported.

### About the bundled ffmpeg

Every package carries ffmpeg 9.0.x. The linux binaries are built from source rather
than taken from a publisher, compiled against musl and linked fully statically, so they
have no interpreter and no libc dependency and run on any linux, including musl based
images such as Alpine and distributions far older than a glibc build would allow.

### GraalVM native image

Supported from 4.2.0 with nothing to configure. The `jave-nativebin-*` packages carry the
reachability metadata that registers the bundled ffmpeg, so native-image keeps it in the
image instead of discarding it. Depend on the single platform package you are building
for rather than `jave-all-deps`, or every binary is embedded. See
[Usage](https://github.com/a-schild/jave2/wiki/Usage#graalvm-native-image) for the
details.

## Projects using Jave2
* [XR3Player](https://github.com/goxr3plus/XR3Player)
* [XR3Converter](https://github.com/goxr3plus/XR3Converter)
* [On Github](https://github.com/a-schild/jave2/network/dependents)
* ....



## Installing

The full documentation is in the [project wiki](https://github.com/a-schild/jave2/wiki),
and the sections below cover getting the dependencies right.

Maven Repository URL -> https://mvnrepository.com/artifact/ws.schild/jave-all-deps

### Use with Maven

It includes all binaries for the supported platforms

<!-- https://mvnrepository.com/artifact/ws.schild/jave-all-deps -->
``` XML
<dependency>
 <groupId>ws.schild</groupId>
 <artifactId>jave-all-deps</artifactId>
 <version>4.1.0</version>
</dependency>
```

### Using the bill of materials

If you pick platform packages yourself rather than taking `jave-all-deps`, import the
BOM and leave the versions off. That way the core and the native binaries cannot drift
apart, which is the usual way this goes wrong.

``` XML
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>ws.schild</groupId>
            <artifactId>jave-bom</artifactId>
            <version>4.1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>ws.schild</groupId>
        <artifactId>jave-core</artifactId>
    </dependency>
    <dependency>
        <groupId>ws.schild</groupId>
        <artifactId>jave-nativebin-linux64</artifactId>
    </dependency>
</dependencies>
```

You can use maven dependencies to include the libraries in your projects.
Include the following in your pom files.
### [ Remember always to check the latest release [here](https://github.com/a-schild/jave2/releases/latest) ]

**Generally if you want to use for one platform or more what you have to do is add the jave-core:**

``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-core</artifactId>
    <version>4.1.0</version>
</dependency>
```

and then the specific jar(s) for your platform(s) :

### For one platform only (Linux 64Bit amd/intel in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-linux64</artifactId>
    <version>4.1.0</version>
</dependency>
```

### For one platform only (Linux 64Bit arm in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-linux-arm64</artifactId>
    <version>4.1.0</version>
</dependency>
```

### For one platform only (Linux 32Bit arm in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-linux-arm32</artifactId>
    <version>4.1.0</version>
</dependency>
```

### For one platform only (Windows 64Bit in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-win64</artifactId>
    <version>4.1.0</version>
</dependency>
```

### For one platform only (Windows arm 64Bit in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-win-arm64</artifactId>
    <version>4.1.0</version>
</dependency>
```

### For one platform only (MACOS apple silicon in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-osxm1</artifactId>
    <version>4.1.0</version>
</dependency>
```

### For one platform only (MACOS intel 64Bit in this case, deprecated)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-osx64</artifactId>
    <version>4.1.0</version>
</dependency>
```

### Use with Gradle

It includes all binaries for the supported platforms

``` XML
compile group: 'ws.schild', name: 'jave-all-deps', version: '4.1.0'
```

### For one platform only (Linux 64Bit in this case)
``` XML
compile group: 'ws.schild', name: 'jave-core', version: '4.1.0'
compile group: 'ws.schild', name: 'jave-nativebin-linux64', version: '4.1.0'
```

### Main Components of Jave2
Jave2 consists of two main components:
1. The `jave-core` dependency, which includes all the java code, which is platform independent
2. The `jave-nativebin-<platform>` dependencies, which include the binary executables per platform

There exists a jave-all-deps project, which includes core and all windows und linux binaries.

---
## A first encoding

``` JAVA
File source = new File("source.wav");
File target = new File("target.mp3");

AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setBitRate(128000);
audio.setChannels(2);
audio.setSamplingRate(44100);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp3");
attrs.setAudioAttributes(audio);

new Encoder().encode(new MultimediaObject(source), target, attrs);
```

`encode()` blocks until the transcoding has finished, and throws `EncoderException`
when it fails, or `InputFormatException` when the source could not be read.

## Documentation

| | |
| --- | --- |
| [Usage](https://github.com/a-schild/jave2/wiki/Usage) | Adding the library, encoding, following progress, reading information about a file |
| [Examples](Examples.md) ([wiki](https://github.com/a-schild/jave2/wiki/Examples)) | Recipes for particular conversions: mp3, wav, mp4/H.264, webm/VP9, amr, album art, joining files, extracting stills, filters |
| [Encoding attributes](https://github.com/a-schild/jave2/wiki/Encoding-Attributes) | Every audio and video setting, what each one does |
| [Custom ffmpeg arguments](https://github.com/a-schild/jave2/wiki/Custom-ffmpeg-arguments) | Reaching ffmpeg options the typed API does not model, and using your own ffmpeg binary |
| [Supported formats](https://github.com/a-schild/jave2/wiki/Supported-formats) | What the bundled ffmpeg reads and writes |
| [javadoc](https://javadoc.io/doc/ws.schild/jave-core/latest/index.html) | The API itself |

A runnable sample lives in
[`DefaultFFMPEGLocatorTest.java`](https://github.com/a-schild/jave2/blob/master/jave-example/src/main/java/ws/schild/jave/example/DefaultFFMPEGLocatorTest.java).

## Changelog

Can be found [**here**](https://github.com/a-schild/jave2/blob/master/Changelog.md)

## Using snapshot builds

Snapshots of the `develop` branch are published to the Sonatype Central Portal
snapshot repository. To consume them, add the repository to your build:

```xml
<repositories>
    <repository>
        <id>central-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
```

## Publishing (maintainers)

Artifacts are published to Maven Central through the
[Sonatype Central Portal](https://central.sonatype.com/) using the
`central-publishing-maven-plugin`. The old OSSRH service (`oss.sonatype.org`)
that this project used previously has been retired by Sonatype.

Publishing runs from the `Publish to Maven Central` GitHub Actions workflow:

* a push to `develop` publishes a snapshot, but only while the poms carry a
  `-SNAPSHOT` version
* creating a GitHub release publishes the release, which is auto-released by
  the plugin (`autoPublish=true`)

The workflow needs these repository secrets:

| Secret | Meaning |
| --- | --- |
| `MAVEN_CENTRAL_USERNAME` | Central Portal user token name (Account &rarr; Generate User Token) |
| `MAVEN_CENTRAL_PASSWORD` | Central Portal user token value |
| `MAVEN_GPG_PRIVATE_KEY` | ASCII armored private key, `gpg --armor --export-secret-keys <KEYID>` |
| `MAVEN_GPG_PASSPHRASE` | Passphrase of that key |

To publish by hand, put the same token in a `central` server entry in your
`~/.m2/settings.xml` and run `mvn deploy`.

## Security

JAVE2 runs the ffmpeg executable and gives Java a typed way to build its command
line. Where that leaves the boundary between this library and the application calling
it, what an application must not do with user input, and an answer to CVE-2023-48909,
are all in [SECURITY.md](https://github.com/a-schild/jave2/blob/master/SECURITY.md).

Please report suspected vulnerabilities through a
[private security advisory](https://github.com/a-schild/jave2/security/advisories/new)
rather than a public issue.

## License

JAVE2 is Free Software and it is licensed under [GPL3 LICENSE](https://github.com/a-schild/jave2/blob/master/LICENSE) 

> You will find a copy of the license bundled into the 
downloadable software distribution.


## Feedback

You can send comments to andre@schild.ws
For bug reports use the github site https://github.com/a-schild/jave2/issues


## Credits

Jave is based on the jave version from Carlo Pelliccia  
The original project page with source code can be found here:

http://www.sauronsoftware.it/projects/jave/
