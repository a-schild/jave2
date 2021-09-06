# JAVE2

## Changelog
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
