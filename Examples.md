# JAVE2

## More advanced examples    
For many use cases you can look at the java-core-tests code on how to use the different classes.

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

