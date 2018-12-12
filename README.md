# JAVE2

The JAVE2 (Java Audio Video Encoder) library is Java wrapper on the ffmpeg
project. Developers can take take advantage of JAVE2 to transcode audio 
and video files from a format to another. In example you can transcode
an **AVI** file to a **MPEG** one, you can change a **DivX** video stream into a
(youtube like) **Flash FLV** one, you can convert a **WAV** audio file to a **MP3** or a
**Ogg Vorbis** one, you can separate and transcode audio and video tracks, you can
resize videos, changing their sizes and proportions and so on.

Many other formats, containers and operations are supported by JAVE2.

## Supported Operating Systems + Requirements 

> JAVE requires **Java 8**

> JAVE can also be easily ported to other OS and hardware configurations, 
see the JAVE manual for details.

| Operating System | Windows x32,x64 | MacOS x32,x64 | Linux x32,x64 |
| ------- | :-----: | :-: | :-----: |
| Supported? | YES | YES  | YES  |

## Projects using Jave2
* [XR3Player](https://github.com/goxr3plus/XR3Player)
* [XR3Converter](https://github.com/goxr3plus/XR3Converter)
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
 <version>2.4.4</version>
</dependency>
```

You can use maven dependencies to include the libraries in your projects.
Include the following in your pom files.
### For all platforms [ Remember always to check the latest release [here](https://github.com/a-schild/jave2/releases/latest) ]

``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-all-deps</artifactId>
    <version>2.4.4</version>
</dependency>
```

### For one platform only (Linux 64Bit in this case)
``` XML
<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-core</artifactId>
    <version>2.4.4</version>
</dependency>

<dependency>
    <groupId>ws.schild</groupId>
    <artifactId>jave-native-linux64</artifactId>
    <version>2.4.4</version>
</dependency>
```

### Use with Gradle

It includes all binaries for the supported platforms

``` XML
compile group: 'ws.schild', name: 'jave-all-deps', version: '2.4.4'
```

### For one platform only (Linux 64Bit in this case)
``` XML
compile group: 'ws.schild', name: 'jave-core', version: '2.4.4'
compile group: 'ws.schild', name: 'jave-native-linux64', version: '2.4.4'
```

### Main Components of Jave2
Jave2 consists of two main components:
1. The `jave-core` dependency, which includes all the java code, which is platform independent
2. The `jave-native-<platform>` dependencies, which include the binary executables per platform

There exists a jave-all-deps project, which includes core and all windows und linux binaries.

---
## Examples

#### Converting any audio to mp3
``` JAVA
try {                                                         
 File source = new File("file path");		                 
 File target = new File("file path);                         
                                                              
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

#### Running the conversion in a separate thread, so it can be aborted (only in version 2.4.4 and up)
``` JAVA 
   ... prepare the encoder just as usual and then start it in a thread ...        
 Runnable task = () -> {                                                           
     try                                                                          
     {                                                                            
            encoder.encode(new MultimediaObject(source), target, attrs, listener);
            // Conversion finished, continue with your code                       
     }                                                                            
     catch (EncoderException ex)                                                  
     {                                                                            
         // Unexpected exception in encoder                                       
     }                                                                            
 };                                                                               
                                                                                  
 Thread thread = new Thread(task);                                                
 thread.start();                                                                  
 TimeUnit.MILLISECONDS.sleep(100);                                                
 encoder.abortEncoding();                                                         
```

#### Converting any audio to mp3 with a progress listener
``` JAVA 
ConvertProgressListener listener = new ConvertProgressListener();      
                                                                       
try {                                                                  
 	File source = new File("file path");		                         
 	File target = new File("file path);                                  
                                                                       
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
 	encoder.encode(new MultimediaObject(source), target, attrs,listener);
                                                                       
} catch (Exception ex) {                                               
 	ex.printStackTrace();                                                
 	succeeded = false;                                                   
}                                                                      

public class ConvertProgressListener implements EncoderProgressListener {
                                                                         
   public ConvertProgressListener() {                                    
    //code                                                               
   }                                                                     
                                                                         
   public void message(String m) {                                       
     //code                                                              
   }                                                                     
                                                                         
   public void progress(int p) {                                         
	                                                                     
     //Find %100 progress                                                
double progress = p / 1000.00;                                           
System.out.println(progress);                                            
                                                                         
   }                                                                     
                                                                         
    public void sourceInfo(MultimediaInfo m) {                           
       //code                                                            
    }                                                                    
 }                                                                                                                                                                                                                     
```

---

## License

JAVE2 is Free Software and it is licensed under [GPL3 LICENSE](https://github.com/a-schild/jave2/blob/master/LICENSE) 

> You will find a copy of the license bundled into the 
downloadable software distribution.


## Feedback

You can send comments to andre@schild.ws
For bug reports use the github site https://github.com/a-schild/jave2/issues

## Changelog
- **2.4.5-SNAPSHOT** 
   - Prepared for next development steps
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
