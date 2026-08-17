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
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.enums.PresetEnum;
import ws.schild.jave.info.VideoSize;

/** @author a.schild */
public class EncodingAttributesTest extends AMediaTest {

  public EncodingAttributesTest() {
    super(null, "EncodingAttributes");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeVideo1() throws Exception {
    System.out.println("testEncodeVideo1 avi to mp4");

    File source = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "testEncodeVideo1.mp4");
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
    video.setCrf(18);
    video.setPreset(PresetEnum.FAST.getPresetName());
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp4");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);
    attrs.setDecodingThreads(1);
    attrs.setEncodingThreads(1);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }
}
