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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

/** @author a.schild */
public class FileVideoEncoderTest extends AMediaTest {

  public FileVideoEncoderTest() {
    super(null, "FileVideoEncoder");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo1() throws Exception {
    System.out.println("testEncodeVideo1");

    File source = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "testEncodeVideo1.3gp");
    Files.deleteIfExists(target.toPath());

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
    attrs.setOutputFormat("3gp");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo2() throws Exception {
    System.out.println("testEncodeVideo2");
    File source = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "testEncodeVideo2.3gp");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libfaac");
    audio.setBitRate(64000);
    audio.setSamplingRate(6400);
    audio.setChannels(2);
    VideoAttributes video = new VideoAttributes();
    video.setCodec("mpeg4");
    video.setBitRate(60000);
    video.setFrameRate(15);
    video.setSize(new VideoSize(160, 120));
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("3gp");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    Encoder encoder = new Encoder();
    PListener listener = new PListener();
    encoder.encode(new MultimediaObject(source), target, attrs, listener);
    assertNotNull(listener.getInfo());
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo3() throws Exception {
    System.out.println("testEncodeVideo3");
    File source = new File(getResourceSourcePath(), "AV36_1.AVI");
    File target = new File(getResourceTargetPath(), "testEncodeVideo3.3gp");
    Files.deleteIfExists(target.toPath());

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
    attrs.setOutputFormat("3gp");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    Encoder encoder = new Encoder();
    PListener listener = new PListener();

    String errorMessage = "Exit code of ffmpeg encoding run is 1";
    EncoderException ex = assertThrows(EncoderException.class, () -> encoder.encode(new MultimediaObject(source), target, attrs, listener));
    assertEquals(errorMessage, ex.getMessage(), "Unexpected error message");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo4() throws Exception {
    System.out.println("testEncodeVideo4");
    File source = new File(getResourceSourcePath(), "AV36_1.AVI");
    File target = new File(getResourceTargetPath(), "testEncodeVideo4.3gp");
    Files.deleteIfExists(target.toPath());

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
    attrs.setOutputFormat("3gp");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    Encoder encoder = new Encoder();
    PListener listener = new PListener();

    String errorMessage = "Exit code of ffmpeg encoding run is 1";
    EncoderException ex = assertThrows(EncoderException.class, () -> encoder.encode(new MultimediaObject(source), target, attrs, listener));
    assertEquals(errorMessage, ex.getMessage(), "Unexpected error message");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo5() throws Exception {
    System.out.println("testEncodeVideo5");
    File source = new File(getResourceSourcePath(), "AV36_1.AVI");
    File target = new File(getResourceTargetPath(), "testEncodeVideo5.flv");
    Files.deleteIfExists(target.toPath());

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
    attrs.setOutputFormat("flv");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    Encoder encoder = new Encoder();
    PListener listener = new PListener();
    encoder.encode(new MultimediaObject(source), target, attrs, listener);
    assertNotNull(listener.getInfo());
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeAudio09() throws Exception {
    System.out.println("testEncodeAudio09");
    File source = new File(getResourceSourcePath(), "testfile09.mp3");
    if (source.exists()) {
      File target = new File(getResourceTargetPath(), "testEncodeAudio09.wav");
      Files.deleteIfExists(target.toPath());

      // Set Audio Attributes
      AudioAttributes audio = new AudioAttributes();
      audio.setCodec("pcm_s16le");
      audio.setChannels(2);
      audio.setSamplingRate(44100);

      // Set encoding attributes
      EncodingAttributes attributes = new EncodingAttributes();
      attributes.setOutputFormat("wav");
      attributes.setAudioAttributes(audio);
      Encoder encoder = new Encoder();
      PListener listener = new PListener();
      encoder.encode(new MultimediaObject(source), target, attributes, listener);
      assertNotNull(listener.getInfo());
      assertTrue(target.exists(), "Output file missing");
    }
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo10() throws Exception {
    System.out.println("testEncodeVideo10");
    File source = new File(getResourceSourcePath(), "private/test10.mpg");
    if (source.exists()) {
      File target = new File(getResourceTargetPath(), "testEncodeVideo10.mp4");
      Files.deleteIfExists(target.toPath());

      AudioAttributes audio = new AudioAttributes();
      audio.setCodec("eac3");
      audio.setBitRate(97000);
      audio.setSamplingRate(48000);
      audio.setChannels(2);
      VideoAttributes video = new VideoAttributes();
      video.setCodec("mpeg4");
      video.setBitRate(1500000);
      video.setFrameRate(30);
      video.setSize(new VideoSize(320, 240));
      EncodingAttributes attrs = new EncodingAttributes();
      attrs.setOutputFormat("mp4");
      attrs.setVideoAttributes(video);
      attrs.setAudioAttributes(audio);
      Encoder encoder = new Encoder();
      PListener listener = new PListener();
      encoder.encode(new MultimediaObject(source), target, attrs, listener);
      assertNotNull(listener.getInfo());
      assertTrue(target.exists(), "Output file missing");
    }
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo11() throws Exception {
    System.out.println("testEncodeVideo11");

    File source = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "testEncodeVideo11.3gp");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libfaac");
    audio.setBitRate(128000);
    audio.setSamplingRate(44100);
    audio.setChannels(2);
    audio.setQuality(31);
    VideoAttributes video = new VideoAttributes();
    video.setCodec("mpeg4");
    video.setBitRate(160000);
    video.setFrameRate(15);
    video.setSize(new VideoSize(176, 144));
    video.setQuality(31);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("3gp");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo12() throws Exception {
    System.out.println("testEncodeVideo12");

    File source = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "testEncodeVideo12.mp4");
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
    encoder.encode(new MultimediaObject(source), target, encodingAttr);
    assertTrue(target.exists(), "Output file missing");
  }
}
