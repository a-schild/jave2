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

/** @author a.schild */
public class FileAudioEncoderTest extends AMediaTest {

  public FileAudioEncoderTest() {
    super(null, "FileAudioEncoder");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeAudio10() throws Exception {
    System.out.println("testEncodeAudio10");

    File source = new File(getResourceSourcePath(), "4channels.ogg");
    File target = new File(getResourceTargetPath(), "4channels.flac");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audioAttr = new AudioAttributes();
    EncodingAttributes encodingAttr = new EncodingAttributes();

    audioAttr.setCodec("flac");
    audioAttr.setBitRate(360000);
    audioAttr.setVolume(1000);
    audioAttr.setQuality(1000);
    audioAttr.setSamplingRate(48000);
    encodingAttr.setOutputFormat("flac");
    encodingAttr.setAudioAttributes(audioAttr);

    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, encodingAttr);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeAudio1() throws Exception {
    System.out.println("testEncodeAudio1");
    File source = new File(getResourceSourcePath(), "Alesis-Fusion-Clean-Guitar-C3.wav");
    File target = new File(getResourceTargetPath(), "testEncodeAudio1.mp3");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    audio.setBitRate(128000);
    audio.setChannels(2);
    audio.setSamplingRate(44100);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
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
  public void testEncodeAudio2() throws Exception {
    System.out.println("testEncodeAudio2");
    File source = new File(getResourceSourcePath(), "Alesis-Fusion-Clean-Guitar-C3.wav");
    File target = new File(getResourceTargetPath(), "testEncodeAudio2.mp3");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    audio.setBitRate(128000);
    audio.setChannels(2);
    audio.setSamplingRate(42100);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
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
  public void testEncodeAudio3() throws Exception {
    System.out.println("testEncodeAudio3");
    File source = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "testEncodeAudio3.mp3");
    Files.deleteIfExists(target.toPath());

    Encoder encoder = new Encoder();
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    audio.setBitRate(128000);
    audio.setChannels(2);
    audio.setSamplingRate(44100);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeAudio4() throws Exception {
    System.out.println("testEncodeAudio4");
    File source = new File(getResourceSourcePath(), "buggy.ogg");
    File target = new File(getResourceTargetPath(), "testEncodeAudio4.mp3");
    Files.deleteIfExists(target.toPath());

    Encoder encoder = new Encoder();
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeAudio5() throws Exception {
    System.out.println("testEncodeAudio5");
    File source = new File(getResourceSourcePath(), "cj2009-10-05d01t07.ku100_at37.flac");
    File target = new File(getResourceTargetPath(), "testEncodeAudio5.mp3");
    Files.deleteIfExists(target.toPath());

    Encoder encoder = new Encoder();
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
    attrs.setMapMetaData(true);
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  @Test
  public void testEncodeAudio6() throws Exception {
    System.out.println("testEncodeAudio6");
    File source = new File(getResourceSourcePath(), "audioonly.mpeg");
    File target = new File(getResourceTargetPath(), "testEncodeAudio6.mp3");
    Files.deleteIfExists(target.toPath());

    Encoder encoder = new Encoder();
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("mp3");
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setDecodingThreads(1);
    attrs.setEncodingThreads(1);
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
    attrs.setMapMetaData(true);
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }
}
