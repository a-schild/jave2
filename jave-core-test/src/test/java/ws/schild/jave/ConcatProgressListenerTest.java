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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MediaConcatFilter;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

/**
 * Concatenating several sources used to hand the listener a null and let it fall over.
 *
 * <p>The source information is read from a single input, so with more than one there is none, and
 * {@code sourceInfo} was called with null all the same. Any listener that reached into what it was
 * given, which is the only reason to implement the method, died of a NullPointerException at the
 * start of every concatenation.
 */
public class ConcatProgressListenerTest extends AMediaTest {

  public ConcatProgressListenerTest() {
    super(null, "ConcatProgressListener");
  }

  /** A listener written the way anyone would write one, reading what it is handed. */
  private static class ReadsWhatItIsGiven implements EncoderProgressListener {
    private boolean gotSourceInfo = false;
    private boolean wasHandedNull = false;

    @Override
    public void sourceInfo(MultimediaInfo info) {
      gotSourceInfo = true;
      if (info == null) {
        wasHandedNull = true;
        return;
      }
      // The line that used to throw
      info.getDuration();
    }

    @Override
    public void progress(int permil) {
      // nothing to do
    }

    @Override
    public void message(String message) {
      // nothing to do
    }
  }

  /**
   * Concatenating two sources has to reach the end without the listener being handed a null.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testListenerIsNotHandedNullWhenConcatenating() throws Exception {
    File source = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "concatProgress.3gp");
    Files.deleteIfExists(target.toPath());

    List<MultimediaObject> sources = new ArrayList<>();
    sources.add(new MultimediaObject(source));
    sources.add(new MultimediaObject(source));

    VideoAttributes video = new VideoAttributes();
    video.setCodec("mpeg4");
    FilterGraph graph = new FilterGraph();
    FilterChain chain = new FilterChain();
    chain.addFilter(new MediaConcatFilter(sources.size(), true, false));
    graph.addChain(chain);
    video.setComplexFiltergraph(graph);

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("aac");

    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("3gp");
    attrs.setVideoAttributes(video);
    attrs.setAudioAttributes(audio);

    ReadsWhatItIsGiven listener = new ReadsWhatItIsGiven();
    new Encoder().encode(sources, target, attrs, listener);

    assertFalse(
        listener.wasHandedNull,
        "sourceInfo was called with null, which is what made listeners throw here");
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * The single source case still reports the information, since that is the whole point of the
   * callback and it would be easy to silence it while fixing the other one.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testListenerStillGetsSourceInfoForASingleSource() throws Exception {
    File source = new File(getResourceSourcePath(), "dance1.avi");
    File target = new File(getResourceTargetPath(), "singleProgress.3gp");
    Files.deleteIfExists(target.toPath());

    VideoAttributes video = new VideoAttributes();
    video.setCodec("mpeg4");
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("3gp");
    attrs.setVideoAttributes(video);

    ReadsWhatItIsGiven listener = new ReadsWhatItIsGiven();
    new Encoder().encode(new MultimediaObject(source), target, attrs, listener);

    assertTrue(listener.gotSourceInfo, "sourceInfo was never called for a single source");
    assertFalse(listener.wasHandedNull, "sourceInfo was called with null for a single source");
    assertTrue(target.exists(), "Output file missing");
  }
}
