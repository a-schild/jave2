# JAVE2
jave-core [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-core?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-core)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-core%2Fmaven-metadata.xml)

jave-all-deps [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-all-deps?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-all-deps)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-all-deps%2Fmaven-metadata.xml)

jave-nativebin-linux-arm32 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux-arm32?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux-arm32)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux-arm32%2Fmaven-metadata.xml)

jave-nativebin-linux-arm64 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux-arm64?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux-arm64)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux-arm64%2Fmaven-metadata.xml)

jave-nativebin-linux32 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux32?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux32)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux32%2Fmaven-metadata.xml)

jave-nativebin-linux64 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-linux64?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-linux64)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-linux64%2Fmaven-metadata.xml)

jave-nativebin-win32 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-win32?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-win32)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-win32%2Fmaven-metadata.xml)

jave-nativebin-win64 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-win64?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-win64)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-win64%2Fmaven-metadata.xml)

jave-nativebin-osx64 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-osx64?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-osx64)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-osx64%2Fmaven-metadata.xml)

jave-nativebin-osxm1 [![Maven Central](https://img.shields.io/maven-central/v/ws.schild/jave-nativebin-osxm1?label=release)](https://central.sonatype.com/artifact/ws.schild/jave-nativebin-osxm1)
![Sonatype Central (Snapshots)](https://img.shields.io/maven-metadata/v?label=snapshot&metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fws%2Fschild%2Fjave-nativebin-osxm1%2Fmaven-metadata.xml)


The JAVE2 (Java Audio Video Encoder) library is Java wrapper on the ffmpeg
project. Developers can take take advantage of JAVE2 to transcode audio 
and video files from a format to another. In example you can transcode
an **AVI** file to a **MPEG** one, you can change a **DivX** video stream into a
(youtube like) **Flash FLV** one, you can convert a **WAV** audio file to a **MP3** or a
**Ogg Vorbis** one, you can separate and transcode audio and video tracks, you can
resize videos, changing their sizes and proportions and so on.

Many other formats, containers and operations are supported by JAVE2.

## Supported Operating Systems + Requirements 

> JAVE requires **Java 8 or higher**

> JAVE can also be easily ported to other OS and hardware configurations, 
see the JAVE manual for details.

| Operating System | Windows x32,x64 | MacOS intel x64 | MacOS m1 | Linux x32,x64 | Linux arm32,arm64 |
| ---------------- | --------------- |  -------------- | -------- | ------------- | ----------------- |
| Supported?       | Partial,YES     | YES             |  YES     | YES           | Partial,YES       |

Please note that the arm+win 32 bit versions are still on 4.4.0 and will be removed in a future release
The win32 binaries will be removed in the next release

## Projects using Jave2
* [XR3Player](https://github.com/goxr3plus/XR3Player)
* [XR3Converter](https://github.com/goxr3plus/XR3Converter)
* [On Github](https://github.com/a-schild/jave2/network/dependents)
* ....



## Usage Example
For the documentation, please have a look at the project wiki pages [**here**](https://github.com/a-schild/jave2/wiki)

or at this file [**DefaultFFMPEGLocatorTest.java**](https://github.com/a-schild/jave2/blob/master/jave-example/src/main/java/ws/schild/jave/example/DefaultFFMPEGLocatorTest.java)


Maven Repository URL -> https://mvnrepository.com/artifact/ws.schild/jave-all-deps

### Use with Maven

It includes all binaries for the supported platforms

<!-- https://mvnrepository.com/artifact/ws.schild/jave-all-deps -->
``` XML
<dependency>
 <groupId>ws.schild</groupId>
 <artifactId>jave-all-deps</artifactId>
 <version>3.5.0</version>
</dependency>
```

You can use maven dependencies to include the libraries in your projects.
Include the following in your pom files.
### [ Remember always to check the latest release [here](https://github.com/a-schild/jave2/releases/latest) ]

**Generally if you want to use for one platform or more what you have to do is add the jave-core:**

``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-core</artifactId>
    <version>3.5.0</version>
</dependency>
```

and then the specific jar(s) for your platform(s) :

### For one platform only (Linux 64Bit amd/intel in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-linux64</artifactId>
    <version>3.5.0</version>
</dependency>
```

### For one platform only (Linux 64Bit arm in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-linux-arm64</artifactId>
    <version>3.5.0</version>
</dependency>
```

### For one platform only (Linux 32Bit arm in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-linux-arm32</artifactId>
    <version>3.5.0</version>
</dependency>
```

### For one platform only (Windows 64Bit in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-win64</artifactId>
    <version>3.5.0</version>
</dependency>
```

### For one platform only (MACOS 64Bit in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-nativebin-osx64</artifactId>
    <version>3.5.0</version>
</dependency>
```

### Use with Gradle

It includes all binaries for the supported platforms

``` XML
compile group: 'ws.schild', name: 'jave-all-deps', version: '3.5.0'
```

### For one platform only (Linux 64Bit in this case)
``` XML
compile group: 'ws.schild', name: 'jave-core', version: '3.5.0'
compile group: 'ws.schild', name: 'jave-nativebin-linux64', version: '3.5.0'
```

### Main Components of Jave2
Jave2 consists of two main components:
1. The `jave-core` dependency, which includes all the java code, which is platform independent
2. The `jave-nativebin-<platform>` dependencies, which include the binary executables per platform

There exists a jave-all-deps project, which includes core and all windows und linux binaries.

---
## Examples

#### Converting any audio to mp3
``` JAVA
boolean succeeded;  
try {                                                         
 File source = new File("file path");		                 
 File target = new File("file path");                         
                                                              
 //Audio Attributes                                       
 AudioAttributes audio = new AudioAttributes();              
 audio.setCodec("libmp3lame");                               
 audio.setBitRate(128000);                                   
 audio.setChannels(2);                                       
 audio.setSamplingRate(44100);                               
                                                             
 //Encoding attributes                                       
 EncodingAttributes attrs = new EncodingAttributes();        
 attrs.setFormat("mp3");                                     
 attrs.setAudioAttributes(audio);                            
                                                             
 //Encode                                                    
 Encoder encoder = new Encoder();                            
 encoder.encode(new MultimediaObject(source), target, attrs);
                                                              
} catch (Exception ex) {                                      
 ex.printStackTrace();                                       
 succeeded = false;                                          
}                                                             
```
     
## More advanced examples    

Can be found [**here**](https://github.com/a-schild/jave2/blob/master/Examples.md)

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
