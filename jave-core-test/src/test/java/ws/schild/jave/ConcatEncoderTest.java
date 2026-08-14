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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MediaConcatFilter;
import ws.schild.jave.info.VideoSize;

/** @author a.schild */
public class ConcatEncoderTest extends AMediaTest {

  public ConcatEncoderTest() {
    super(null, "ConcatEncoder");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testConcatVideo1() throws Exception {
    System.out.println("concat two identical videos");

    File source1 = new File(getResourceSourcePath(), "dance1.avi");
    File source2 = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "testConcatVideo1.3gp");
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
    List<MultimediaObject> src = new ArrayList<>();
    src.add(new MultimediaObject(source1));
    src.add(new MultimediaObject(source2));
    FilterGraph complexFiltergraph = new FilterGraph();
    FilterChain fc = new FilterChain();
    fc.addFilter(new MediaConcatFilter(src.size(), true, false));
    complexFiltergraph.addChain(fc);
    video.setComplexFiltergraph(complexFiltergraph);
    encoder.encode(src, target, attrs);

    assertTrue(target.exists(), "Output file missing");
    assertEquals(107872, target.length(), "Output file incorrect size");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testConcatVideo2() throws Exception {
    System.out.println("concat two identical videos");

    File source1 = new File(getResourceSourcePath(), "small.mp4");
    File source2 = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "testConcatVideo2.mp4");
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

    List<MultimediaObject> src = new ArrayList<>();
    src.add(new MultimediaObject(source1));
    src.add(new MultimediaObject(source2));

    FilterGraph complexFiltergraph = new FilterGraph();
    FilterChain fc = new FilterChain();
    fc.addFilter(new MediaConcatFilter(src.size()));
    complexFiltergraph.addChain(fc);
    video.setComplexFiltergraph(complexFiltergraph);

    encoder.encode(src, target, attrs);
    assertTrue(target.exists(), "Output file missing");
    assertEquals(1360228, target.length(), "Output file incorrect size");
  }

  @Test
  public void testContactAudio01() throws Exception {
    System.out.println("concat two wmv files and build wav from it");
    File source1 = new File(getResourceSourcePath(), "testfile3.wmv");
    File source2 = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "testContactAudio01.wav");
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
    List<MultimediaObject> src = new ArrayList<>();
    src.add(new MultimediaObject(source1));
    src.add(new MultimediaObject(source2));
    FilterGraph complexFiltergraph = new FilterGraph();
    FilterChain fc = new FilterChain();
    fc.addFilter(new MediaConcatFilter(src.size(), false, true));
    complexFiltergraph.addChain(fc);
    VideoAttributes video = new VideoAttributes();
    video.setComplexFiltergraph(complexFiltergraph);
    attributes.setVideoAttributes(video);
    encoder.encode(src, target, attributes);
    assertTrue(target.exists(), "Output file missing");
    assertEquals(20477182, target.length(), "Output file incorrect size");
  }
}
