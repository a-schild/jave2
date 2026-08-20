# JAVE2

## Changelog
- **4.1.0-SNAPSHOT**
   - Fixed nonsense progress values for sources with no declared duration. A percentage
     needs a total, and live streams, webm files from browser recorders and concatenated
     sources have none. The permil was calculated anyway, dividing by a duration of -1 or
     0, which produced a large negative number that slipped past the upper clamp and
     reached the listener as if it were progress. `EncoderProgressListener.progress()` is
     now called with the new `EncoderProgressListener.PROGRESS_UNKNOWN` constant in those
     cases, and a calculated permil is clamped to 0..1000 (#269)
   - Rewrote the documentation, which had drifted a long way from the library. The wiki
     `Usage` page still documented version 2.4.2, `attrs.setFormat()` and the old
     `jave-native-*` artifact names, and every example on the `Examples` page used
     `setFormat`, which has not existed for years, so none of them compiled.
     `Encoding-Attributes` was regenerated from the source: it had listed six setters
     under the wrong package names and omitted `crf`, `preset`, `tune`, `x264Profile`,
     `pixelFormat`, `faststart`, `quality`, `vsync` and the filters entirely (#59, #155)
   - New wiki page, `Custom ffmpeg arguments`, on reaching options the typed API does
     not model, driving ffmpeg through `ProcessWrapper`, and supplying your own binary
     with a `ProcessLocator` (#155)
   - Regenerated the wiki `Supported formats` page from the bundled ffmpeg 9.0.1. It
     predated this fork, listing `liba52`, `libfaac`, `libfaad`, `libamr_nb`,
     `libamr_wb`, `sonic` and `sonicls`, which ffmpeg dropped over a decade ago, while
     omitting `libopus`, `libvpx-vp9`, `libx265`, `libaom-av1`, `aac` and `libwebp`.
     It now also documents the `Encoder.get*Encoders()` query methods, which never go
     stale, and which parts of the codec set differ between platforms
   - Expanded the wiki `Developers guide lines` page, which was seven lines about
     semantic versioning, to cover building and the Java 8 language level, the module
     layout, the test suite, branches, how the static musl linux binaries are built
     and what the CI parity gate checks, and releasing through the Central Portal
   - The usage examples have moved out of the README into `Examples.md` and the matching
     wiki page, so there is one place to keep current instead of three. The README keeps
     a single first encoding and points at the rest. `Examples.md` gains recipes for
     video to audio, audio only mp4, joining audio files, AMR, volume, H.264, VP9,
     trimming, sideways phone video, stills, filters and aborting a running encoding
     (#59)
- **4.0.0**
   - New package `jave-nativebin-win-arm64`, ffmpeg 9.0.1 for windows on arm. It is
     part of `jave-all-deps`, and needs no code change because the executable is
     already looked up by `os.arch`, which reports `aarch64` there
   - Upgraded the bundled ffmpeg from 4.4.1 to 9.0.x on every platform: windows 64
     bit and windows on arm, both macOS packages, linux 64 bit, and linux arm 64 and
     32 bit
   - The linux binaries are now built from source rather than taken from a publisher,
     because the static builds this project used are no longer reachable and every
     remaining publisher links dynamically against glibc 2.28 or newer. Ours link
     against musl and need no libc at all, so they run on any linux, including musl
     based images and distributions far older than those builds would allow
   - Fixed `EncoderProgressListener.sourceInfo()` being called with null when several
     sources are concatenated. The information is read from a single input, so with
     more than one there is none, and any listener that read what it was handed threw
     a NullPointerException at the start of every concatenation (#178)
   - Added `Encoder.getOptionAtIndex()`. Reading an option was already possible but
     the method was called `setOptionAtIndex`, which is presumably why nobody found
     it. The old name still works and is deprecated (#180)
   - New `jave-bom` package, a bill of materials. Import it under
     `dependencyManagement` with `scope` import and the jave artifacts can be declared
     without versions, so a project that picks its own platform packages cannot end up
     with a core and a native binary from different releases (#273)
   - Documented how to keep album art when converting audio. Cover art is a video
     stream, so an encoding with no VideoAttributes drops it along with everything
     else video. Setting a VideoAttributes with codec copy keeps it, which is now in
     Examples.md and covered by tests (#266)
   - Added SECURITY.md, setting out where the boundary is between what this library
     is responsible for and what the calling application is, and answering
     CVE-2023-48909 with the two properties that make its claim untrue: no shell is
     ever spawned, and paths are absolutised so they cannot be read as ffmpeg
     options. Both are now covered by tests
   - Deprecated `jave-nativebin-osx64`, the package for intel macs. Apple ends support
     for intel hardware with macOS 27, so it will be removed in a later release. It is
     still built, still published and still part of `jave-all-deps`, so nothing breaks
     yet. On apple silicon use `jave-nativebin-osxm1`, which is unaffected
   - **Breaking:** removed the 32 bit x86 packages `jave-nativebin-win32` and
     `jave-nativebin-linux32`. ffmpeg no longer publishes builds for 32 bit Windows,
     so that binary could not be kept current, and 32 bit x86 Linux goes with it.
     Stay on 3.6.0 if you need either. 32 bit ARM is not affected and remains
     supported
   - Fixed `VideoAttributes.setVsync()` failing on ffmpeg 5 and newer, which removed
     `-vsync` in favour of `-fps_mode`. The two never overlap, so the option is now
     chosen from what the ffmpeg actually in use accepts, asked once per executable.
     Note that `VsyncMethod.DROP` has no counterpart under `-fps_mode`
   - Fixed `getSupportedEncodingFormats()` and `getSupportedDecodingFormats()`
     returning an empty array against ffmpeg 5 and newer. They looked for the
     header `File formats:`, which ffmpeg now calls `Formats:`, and the rule of
     dashes under it grew a column. Nothing reported the mismatch, the list was
     simply empty
   - Fixed `AudioAttributes.setVolume()` killing the encoding on ffmpeg 5 and
     newer. It was passed as `-vol`, an option ffmpeg has removed, so the run died
     on `Unrecognized option 'vol'`. It now uses the volume filter, with the value
     converted from the 256 based scale so callers do not have to change anything
   - Test assertions that pinned ffmpeg exit codes and an exact duration now
     accept what any ffmpeg release reports, since neither is part of this library
- **3.6.0**
   - Fixed the extracted ffmpeg binary being run before it was ready. The chmod was
     started but never waited for, and the copy went straight to the target path, so
     a second process could pick up a half written or not yet executable file and
     fail with "Permission denied" or "Cannot run program" (#281, #236)
   - Fixed `Encoder`'s shared option list throwing a ConcurrentModificationException
     when it was changed while another thread was encoding, and `VideoProcessor`
     reporting itself enabled because some earlier instance had been (#179)
   - Added `MultimediaObject.getInfo(long timeoutMillis)`, so an unreachable or
     stalled source can no longer block the call forever (#264)
   - Corrected `attrs.setFormat(...)` to `setOutputFormat(...)` in README.md and
     Examples.md, the old name does not exist on EncodingAttributes (#189)
   - Added `MultimediaInfo.getRotate()`, the rotation cameras record in the stream
     metadata when filming in a non native orientation, thanks to JinLike
   - Added `EncoderProgressListener.done()`, called once an encoding has finished.
     It is a default method, so existing listeners keep working unchanged,
     thanks to JinLike
   - Added `EncodingAttributes.setStreamLoop()` for ffmpeg `-stream_loop`,
     thanks to supermoonie
   - Added `ProcessWrapper.setExecFolder()` to choose the working directory of the
     ffmpeg process, thanks to supermoonie
   - Added `MultimediaInfo.getMultimediaObject()`, pointing back at the object the
     information was read from, thanks to supermoonie
   - An unparsable source media header no longer aborts the whole encoding, it is
     only needed to report progress as a percentage, thanks to jhsea3do
   - Reworked the unit tests, thanks to Stickerifier
   - Fixed the test setup, which used a surefire version predating the junit
     platform. JUnit 5 annotations had no effect, `@Disabled` was ignored and seven
     tests were never run
   - Migrated publishing from the retired OSSRH service (oss.sonatype.org) to the
     Sonatype Central Portal. The nexus-staging-maven-plugin has been replaced by
     the central-publishing-maven-plugin in all published modules
   - Added a "Publish to Maven Central" GitHub Actions workflow, snapshots are
     published from the develop branch, releases from GitHub releases
   - Fixed the version badges in README.md, the old OSSRH service they queried has
     been retired. Release badges now read from Maven Central, snapshot badges from
     the Central Portal snapshot repository
- **3.5.0**
   - Added support for Tune video attribute, thanks to rayacode
- **3.4.0**
   - Added PresetEnum to API
   - Added quit encoding to api, thanks to sam80180
   - Added module name for java 9+ compatibility, thanks to Stickerifier
   - Use proper class for semaphore, thanks to leeychee
   - Updated maven build environment
- **3.3.1**
   - Fixed naming of binary for OSX M1 platform
   - Windows 64bit + 32bit binary on 4.4.1 too
    -> Still missing 4.4.1 binaries for arm32 build
- **3.3.0**
   - Upgraded slf4j libraries
   - Added options to get/set options by index, thanks to HANXU2018
   - Upgraded binaries to 4.4.1 release
     OS-X releases from https://www.osxexperts.net/
	 Linux binaries from https://johnvansickle.com/ffmpeg/
	 -> Windows and arm32 bit builds still on 4.4.0 release
- **3.2.1**
   - Moved development to it's own branch
   - Implemented first support for apple m1 chip on OS-X (Needs to be tested)
   - Added win 32 ffmpeg 4.4 static release https://www.notion.so/34dc4ddf501a4b98b46ea9fb4f3470af?v=878345c5d88f4d21a6520db752b5c29f
- **3.2.0**
   - Modified quoting for command line arguments
   - Implemented subtitle ass video filter
   - Added constructor for scaling filter which allows string expressions
   - Added constructor for scaling filter which does not require the ForceOriginalAspectRatio parameter
   - Added CropFilter
   - Added constructor for color filter which allows string expressions
   - Added support for multiple video filters in one conversion pass
   - Added enhanced meta data detection in MultiMedia object
   - Implement critical section in executable location+creation to prevent race condition (Issue #163)
   - Upgraded to ffmpeg v 4.4
   - Binaries from https://github.com/eugeneware/ffmpeg-static 
   - The 32bit windows binaries remain at v4.2 since ffmpeg no longer supports the 32bit architekture
     32bit support will be removed later
- **3.1.0**
   - Added support for arm32 bit (Thanks to jmformenti)
   - Added option to use a specific quote character for command line
     options. (Thanks to topcatv)
   - Added support for multimedia metdata (Thanks to jmformenti)
   - Corrected typo in setURL method of MultimediaObject (Thanks to Pyjou)
- **3.0.1**
   - Fixed a class loader issue when using it in spring boot environments
- **3.0.0**
   - Reworked base classes to handle the executable (Thanks to Michael Ressler)
   - Reworked the API to have a fluent and more flexible api (Thanks to Michael Ressler)
   - Added more supporting methods/classes to video processing/transformations
   
- **2.8.0**
   - Added -ss option to ScreenExtractor for faster processing
   - Add loopAttribute to EncodingAttributes, thanks to chrysophylax
     https://github.com/a-schild/jave2/pull/79
   - Added support for arm64 linux
   - Some code cleanups by mressler, thanks for the contribution
   - Deprecation of ws.schild.jave.FFMPEG* related process stuff,
     use ws.schild.jave.process.* instead
- **2.7.4**
   - Upgraded to ffmpeg v 4.2.2
   - Windows and osx binaries from https://ffmpeg.zeranoe.com/builds/  
   - Linux binaries from https://johnvansickle.com/ffmpeg/  
- **2.7.3**
   - Close class reference when binary was extracted from jar file
- **2.7.2**
   - Handle invalid/unknown duration values in containers
- **2.7.1**
   - Allow additional arguments on watermark filter
   - Make positions optional (use -1 for posX and posY) and then use the setAddArgument() method
- **2.7.1**
   - Make FFMPEGLocator.createExecutor() to allow it to be used in other contexts
   - added execute method to FFMPEGExecutor to leave the ffmpeg processes running after JVM shutdown
   - First implementation of drawtext filter
   - Most support classes now return the instance when calling setXY() methods
- **2.6.1** 
   - Added additional method to screen extractor to extract a given image from a video
     at the give time in milisenconds, and optional, specify size of resulting image
- **2.6.0** 
   - Change of logging api to slf4j as logging facade in version 1.7.x
   - The encode methods now accept a List<> of MultimediaObject's, which are the concatenated
   - Implemented set thread cound for encoding / decoding stages (Max number of cores/cpus to use)
   - URL's as source are now treated as "multiple time readables" per default, can be overriden in the MultimediaObject
   - Progress listener now works for URL's too, unless the "multiple time readables" flag is set to true
- **2.5.1** 
   - Renamed native folder where the executables are to nativebin too, to prevent Java 9+ problems
   - Allow to create a MultimediaObject with an URL instead of a file as conversion source
   - Enhanced logging when ffmpeg executable could not be found in resources
   - Make getFFMPEGExecutablePath() public to see what executable is beeing used
   - Use alternative way to locate executables in jar file for Java 9+
- **2.5.0** 
   - Renamed build artifacts with native in the name to nativebin to avoid module conflicts in Java 9+
     You will have to change your build dependencies if you did selectively include platforms
- **2.4.7** 
   - Upgraded maven build infrastructure
   - Upgraded windows and osx binaries to 4.1.3 from https://ffmpeg.zeranoe.com/builds/  
   - Upgraded linux binaries to 4.1.3 from https://johnvansickle.com/ffmpeg/  
- **2.4.6** 
   - Corrected win32 executable name, thanks to WavyPeng
   - The bitrate was returned in kBps instead of Bps
   - The channel detection did only work for mono+stereo, but not for quad (It does still not work for others)
   - Added javadoc about volume and quality values
- **2.4.5** 
   - Added video and audio quality flags for conversion (see VideoAttributes.quality and AudioAttributes.quality)
   - Changed aac de/encoder from libvo_aacenc to default aac settings from ffmpeg
   - Upgraded windows and osx binaries to 4.1 from https://ffmpeg.zeranoe.com/builds/  
   - Upgraded linux binaries to 4.1 from https://johnvansickle.com/ffmpeg/  
- **2.4.4** 
   - More informative error message when not finding ffmpeg executable
   - Added option to copy over meta data if possible (setMapMetaData(true) in EncodingAttributes)
   - Better handling of process exit code
- **2.4.3** 
   - Upgraded windows and osx binaries to 4.0.2 from https://ffmpeg.zeranoe.com/builds/  
   - Upgraded linux binaries to 4.0.2 from https://johnvansickle.com/ffmpeg/  
   - Made output handling more robust,   
   - we now only throw an encoder exception when encoder exit code is not 0  
   - Unknown conversion lines can betrieved via encoder.getUnhandledMessages()  
   - Added abortEncoding method to be able to stop the running encoder  
- **2.4.2** 
   - Enhanced output parsing when using copy operator for streams  
   - Refactoring of outpout analyzer in own class for simpler unit tests  
- **2.4.1** 
   - Allow conversion of "corrupt" input files, as generated by some softwares
- **2.4.0** 
   - Renaming packages to ws.schild.jave for publishing in maven central  
   - First version released via maven central

## Credits

Jave is based on the jave version from Carlo Pelliccia  
The original project page with source code can be found here:

http://www.sauronsoftware.it/projects/jave/
