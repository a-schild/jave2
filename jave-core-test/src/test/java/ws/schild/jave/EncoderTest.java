/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 * 
 * Copyright (C) 2018- Andre Schild
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ws.schild.jave;

import java.io.File;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author a.schild
 */
public class EncoderTest extends AMediaTest{
    
    public EncoderTest() {
        super(null, "EncoderTest");
    }

    /**
     * Test of getAudioDecoders method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAudioDecoders() throws Exception {
        System.out.println("getAudioDecoders");
        Encoder instance = new Encoder();
        String[] result = instance.getAudioDecoders();
        assertTrue(result != null && result.length >0, "No audio decoders found");
    }

    /**
     * Test of getAudioEncoders method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAudioEncoders() throws Exception {
        System.out.println("getAudioEncoders");
        Encoder instance = new Encoder();
        String[] result = instance.getAudioEncoders();
        assertTrue(result != null && result.length >0, "No audio encoders found");
    }

    /**
     * Test of getVideoDecoders method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetVideoDecoders() throws Exception {
        System.out.println("getVideoDecoders");
        Encoder instance = new Encoder();
        String[] result = instance.getVideoDecoders();
        assertTrue(result != null && result.length >0, "No video decoders found");
    }

    /**
     * Test of getVideoEncoders method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetVideoEncoders() throws Exception {
        System.out.println("getVideoEncoders");
        Encoder instance = new Encoder();
        String[] result = instance.getVideoEncoders();
        assertTrue(result != null && result.length >0, "No video enecoders found");
    }

    /**
     * Test of getSupportedEncodingFormats method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetSupportedEncodingFormats() throws Exception {
        System.out.println("getSupportedEncodingFormats");
        Encoder instance = new Encoder();
        String[] result = instance.getSupportedEncodingFormats();
        assertTrue(result != null && result.length >0, "No supported encoding formats found");
    }

    /**
     * Test of getSupportedDecodingFormats method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetSupportedDecodingFormats() throws Exception {
        System.out.println("getSupportedDecodingFormats");
        Encoder instance = new Encoder();
        String[] result = instance.getSupportedDecodingFormats();
        assertTrue(result != null && result.length >0, "No supported decoding formats found");
    }

    /**
     * Test of encode method, of class Encoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testAbortEncoder() throws Exception {
        System.out.println("testAbortEncoder");
        File source = new File(getResourceSourcePath(), "testfile3.wmv");
        File target = new File(getResourceTargetPath(), "testAbortEncoder.mp3");
        if (target.exists())
        {
            target.delete();
        }
        Encoder encoder = new Encoder();
        PListener listener = new PListener();
        String message= null;
        String compareTo= "Specified sample rate";
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        Runnable task = () -> {
            try
            {
                encoder.encode(new MultimediaObject(source), target, attrs, listener);
                assertTrue(target.exists(), "Output file missing");
            }
            catch (EncoderException ex)
            {
                throw new AssertionError("Unexpected exception in encoder", ex);
            }
        };
        
        Thread thread = new Thread(task);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(100);
        encoder.abortEncoding();
    }
    
}
