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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

/** @author a.schild */
public class URLVideoEncoderTest extends AMediaTest {

  public URLVideoEncoderTest() {
    super(null, "URLVideoEncoder");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo13() throws Exception {
    System.out.println("testEncodeVideo13");

    URL source = new URL("https://samples.ffmpeg.org/MPEG1/zelda%20first%20commercial.mpeg");
    File target = new File(getResourceTargetPath(), "testEncodeVideo13.mp4");
    Files.deleteIfExists(target.toPath());
    AudioAttributes audioAttr = new AudioAttributes();
    VideoAttributes videoAttr = new VideoAttributes();
    EncodingAttributes encodingAttr = new EncodingAttributes();

    audioAttr.setChannels(2);
    audioAttr.setCodec("aac");
    audioAttr.setBitRate(128000);
    audioAttr.setSamplingRate(44100);

    videoAttr.setCodec("libx264");
    videoAttr.setBitRate(4000000);

    encodingAttr.setAudioAttributes(audioAttr);
    encodingAttr.setVideoAttributes(videoAttr);
    encodingAttr.setOutputFormat("mp4");

    PListener listener = new PListener();
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source, false), target, encodingAttr, listener);
    assertNotNull(listener.getInfo(), "URL should be able to read twice");
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo14() throws Exception {
    System.out.println("testEncodeVideo14");

    URL source = new URL("https://samples.ffmpeg.org/MPEG1/zelda%20first%20commercial.mpeg");
    File target = new File(getResourceTargetPath(), "testEncodeVideo14.mp4");
    Files.deleteIfExists(target.toPath());
    AudioAttributes audioAttr = new AudioAttributes();
    VideoAttributes videoAttr = new VideoAttributes();
    EncodingAttributes encodingAttr = new EncodingAttributes();

    audioAttr.setChannels(2);
    audioAttr.setCodec("aac");
    audioAttr.setBitRate(128000);
    audioAttr.setSamplingRate(44100);

    videoAttr.setCodec("libx264");
    videoAttr.setBitRate(4000000);

    encodingAttr.setAudioAttributes(audioAttr);
    encodingAttr.setVideoAttributes(videoAttr);
    encodingAttr.setOutputFormat("mp4");

    Encoder encoder = new Encoder();
    PListener listener = new PListener();
    encoder.encode(new MultimediaObject(source, true), target, encodingAttr, listener);
    assertNull(listener.getInfo(), "URL should not be read twice");
    assertTrue(target.exists(), "Output file missing");
  }
}
