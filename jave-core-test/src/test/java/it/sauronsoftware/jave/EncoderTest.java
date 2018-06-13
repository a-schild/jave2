/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sauronsoftware.jave;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a.schild
 */
public class EncoderTest {
    
    public EncoderTest() {
    }

    /**
     * Test of getAudioDecoders method, of class Encoder.
     */
    @Test
    public void testGetAudioDecoders() throws Exception {
        System.out.println("getAudioDecoders");
        Encoder instance = new Encoder();
        String[] result = instance.getAudioDecoders();
        assertTrue("No audio decoders found", result != null && result.length >0);
    }

    /**
     * Test of getAudioEncoders method, of class Encoder.
     */
    @Test
    public void testGetAudioEncoders() throws Exception {
        System.out.println("getAudioEncoders");
        Encoder instance = new Encoder();
        String[] result = instance.getAudioEncoders();
        assertTrue("No audio encoders found", result != null && result.length >0);
    }

    /**
     * Test of getVideoDecoders method, of class Encoder.
     */
    @Test
    public void testGetVideoDecoders() throws Exception {
        System.out.println("getVideoDecoders");
        Encoder instance = new Encoder();
        String[] result = instance.getVideoDecoders();
        assertTrue("No video decoders found", result != null && result.length >0);
    }

    /**
     * Test of getVideoEncoders method, of class Encoder.
     */
    @Test
    public void testGetVideoEncoders() throws Exception {
        System.out.println("getVideoEncoders");
        Encoder instance = new Encoder();
        String[] result = instance.getVideoEncoders();
        assertTrue("No video enecoders found", result != null && result.length >0);
    }

    /**
     * Test of getSupportedEncodingFormats method, of class Encoder.
     */
    @Test
    public void testGetSupportedEncodingFormats() throws Exception {
        System.out.println("getSupportedEncodingFormats");
        Encoder instance = new Encoder();
        String[] result = instance.getSupportedEncodingFormats();
        assertTrue("No supported encoding formats found", result != null && result.length >0);
    }

    /**
     * Test of getSupportedDecodingFormats method, of class Encoder.
     */
    @Test
    public void testGetSupportedDecodingFormats() throws Exception {
        System.out.println("getSupportedDecodingFormats");
        Encoder instance = new Encoder();
        String[] result = instance.getSupportedDecodingFormats();
        assertTrue("No supported decoding formats found", result != null && result.length >0);
    }

    /**
     * Test of encode method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testEncode3args() throws Exception {
        System.out.println("encode");
        Logger.getLogger("it.sauronsoftware.jave.FFMPEGExecutor").setLevel(Level.FINEST);
        
        File source = new File("src/test/resources/dance1.avi");
        File target = new File("target/testoutput/target3.3gp");
        if (target.exists())
        {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(176, 144));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
        assertTrue("Output file missing", target.exists());
    }

    /**
     * Test of encode method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testEncode4args() throws Exception {
        System.out.println("encode");
        Logger.getLogger("it.sauronsoftware.jave.FFMPEGExecutor").setLevel(Level.FINEST);
        File source = new File("src/test/resources/dance1.avi");
        File target = new File("target/testoutput/target4.3gp");
        if (target.exists())
        {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(176, 144));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        PListener listener = new PListener();
        encoder.encode(new MultimediaObject(source), target, attrs, listener);
        assertNotNull(listener.getInfo());
        assertTrue("Output file missing", target.exists());
    }

    /**
     * Test of encode method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testEncode4argsLarge() throws Exception {
        System.out.println("encode");
        Logger.getLogger("it.sauronsoftware.jave.FFMPEGExecutor").setLevel(Level.FINEST);
        File source = new File("src/test/resources/AV36_1.AVI");
        File target = new File("target/testoutput/target4Large.3gp");
        if (target.exists())
        {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(176, 144));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        PListener listener = new PListener();
        String message= null;
        String compareTo= "Unknown encoder 'libfaac'";
        try
        {
            encoder.encode(new MultimediaObject(source), target, attrs, listener);
        }
        catch (EncoderException ex)
        {
            message= ex.getMessage();
        }
        assertEquals("Encoding problem not found", compareTo, message);
    }
    
    /**
     * Test of encode method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testEncode4argsLarge2() throws Exception {
        System.out.println("encode");
        Logger.getLogger("it.sauronsoftware.jave.FFMPEGExecutor").setLevel(Level.FINEST);
        File source = new File("src/test/resources/AV36_1.AVI");
        File target = new File("target/testoutput/target4Large2.3gp");
        if (target.exists())
        {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("adpcm_ms");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(176, 144));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        PListener listener = new PListener();
        String message= null;
        String compareTo= "codec not currently supported in container";
        try
        {
            encoder.encode(new MultimediaObject(source), target, attrs, listener);
        }
        catch (EncoderException ex)
        {
            message= ex.getMessage();
        }
        assertTrue("Encoding problem not found", message.contains(compareTo));
    }
    
    /**
     * Test of encode method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testEncode4argsLarge3() throws Exception {
        System.out.println("encode");
        Logger.getLogger("it.sauronsoftware.jave.FFMPEGExecutor").setLevel(Level.FINEST);
        File source = new File("src/test/resources/AV36_1.AVI");
        File target = new File("target/testoutput/target4Large3.flv");
        if (target.exists())
        {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(64000);
        audio.setChannels(1);
        audio.setSamplingRate(22050);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("flv");
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(400, 300));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("flv");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        PListener listener = new PListener();
        encoder.encode(new MultimediaObject(source), target, attrs, listener);
        assertNotNull(listener.getInfo());
        assertTrue("Output file missing", target.exists());
    }
    
    
    protected class PListener implements EncoderProgressListener
    {
        private MultimediaInfo _info= null;
        private final List<String> _messages= new LinkedList<>();
        private final List<Integer> _progress= new LinkedList<>();
        
        @Override
        public void sourceInfo(MultimediaInfo info) {
            _info= info;
        }

        @Override
        public void progress(int permil) {
            _progress.add(permil);
        }

        @Override
        public void message(String message) {
            _messages.add(message);
        }

        /**
         * @return the _info
         */
        public MultimediaInfo getInfo() {
            return _info;
        }

        /**
         * @return the _messages
         */
        public List<String> getMessages() {
            return _messages;
        }

        /**
         * @return the _progress
         */
        public List<Integer> getProgress() {
            return _progress;
        }
        
    }
}
