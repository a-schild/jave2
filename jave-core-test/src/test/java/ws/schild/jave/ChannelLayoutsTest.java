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
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.utils.ChannelLayouts;
import ws.schild.jave.utils.RBufferedReader;

/**
 * Checks {@link ChannelLayouts} against the bundled ffmpeg rather than against a list written by
 * hand, so that a layout ffmpeg adds or renames shows up here instead of silently going
 * unrecognised (#45).
 *
 * @author a.schild
 */
public class ChannelLayoutsTest extends AMediaTest {

  public ChannelLayoutsTest() {
    super(null, "ChannelLayouts");
  }

  /** One row of {@code ffmpeg -layouts}: a name, and the channels it is made of. */
  private static class Layout {
    final String name;
    final int channels;

    Layout(String name, int channels) {
      this.name = name;
      this.channels = channels;
    }
  }

  /**
   * Reads the standard layouts out of {@code ffmpeg -layouts}. Each row is a name and a
   * decomposition such as {@code FL+FR+FC+LFE+SL+SR}, so the channel count is the number of parts
   * in the decomposition, which makes ffmpeg itself the authority on the expected answer.
   */
  private List<Layout> standardLayouts() throws Exception {
    List<Layout> layouts = new ArrayList<>();
    ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
    try {
      ffmpeg.addArgument("-hide_banner");
      ffmpeg.addArgument("-layouts");
      ffmpeg.execute();

      // The informational options write to stdout, unlike the progress of an encoding.
      RBufferedReader reader = new RBufferedReader(new InputStreamReader(ffmpeg.getInputStream()));
      boolean inStandard = false;
      String line;
      while ((line = reader.readLine()) != null) {
        String trimmed = line.trim();
        if (trimmed.startsWith("Standard channel layouts")) {
          inStandard = true;
          continue;
        }
        if (!inStandard || trimmed.isEmpty() || trimmed.startsWith("NAME")) {
          continue;
        }
        String[] parts = trimmed.split("\\s+");
        // A decomposition is channel names joined by "+", and mono is the single name "FC",
        // so matching on the shape rather than on containing a "+" keeps mono in the check.
        if (parts.length != 2 || !parts[1].matches("[A-Z0-9]+(\\+[A-Z0-9]+)*")) {
          continue;
        }
        layouts.add(new Layout(parts[0], parts[1].split("\\+").length));
      }
    } finally {
      ffmpeg.destroy();
    }
    return layouts;
  }

  /** Every layout the bundled ffmpeg knows resolves to the right number of channels. */
  @Test
  public void testEveryStandardLayoutIsUnderstood() throws Exception {
    List<Layout> layouts = standardLayouts();

    assertTrue(
        layouts.size() > 20,
        "Expected to read the layout table from ffmpeg, got " + layouts.size() + " rows");

    for (Layout layout : layouts) {
      assertEquals(
          layout.channels,
          ChannelLayouts.channelCount(layout.name),
          "Wrong channel count for layout " + layout.name);
    }
    System.out.println("Checked " + layouts.size() + " channel layouts against ffmpeg");
  }

  /** The shapes that reach the parser, including ffmpeg's unnamed fallback. */
  @Test
  public void testLayoutsAsTheyAppearInAStreamLine() {
    assertEquals(1, ChannelLayouts.channelCount("mono"));
    assertEquals(2, ChannelLayouts.channelCount("stereo"));
    assertEquals(4, ChannelLayouts.channelCount("quad"));

    // The bracketed qualifier says which channels, not how many.
    assertEquals(6, ChannelLayouts.channelCount("5.1(side)"));
    assertEquals(6, ChannelLayouts.channelCount("5.1"));
    assertEquals(4, ChannelLayouts.channelCount("quad(side)"));
    assertEquals(7, ChannelLayouts.channelCount("6.1(back)"));

    // Height layouts add a third group.
    assertEquals(8, ChannelLayouts.channelCount("7.1"));
    assertEquals(12, ChannelLayouts.channelCount("7.1.4"));
    assertEquals(24, ChannelLayouts.channelCount("22.2"));

    // No name for the layout, so ffmpeg reports the bare count.
    assertEquals(6, ChannelLayouts.channelCount("6 channels"));
    assertEquals(1, ChannelLayouts.channelCount("1 channel"));
    assertEquals(6, ChannelLayouts.channelCount("6 channels (FL+FR+FC+LFE+BL+BR)"));

    assertEquals(2, ChannelLayouts.channelCount(" STEREO "));
  }

  /**
   * The whole way through: a real 5.1 file is reported as six channels by {@link
   * MultimediaObject}, which is what #45 was about. The file is made with ffmpeg rather than
   * committed, so there is no multichannel fixture to carry around.
   */
  @Test
  public void testMultichannelFileReportsItsChannels() throws Exception {
    File target = new File(getResourceTargetPath(), "sixChannels.m4a");
    Files.deleteIfExists(target.toPath());

    ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
    try {
      ffmpeg.addArgument("-hide_banner");
      ffmpeg.addArgument("-y");
      ffmpeg.addArgument("-f");
      ffmpeg.addArgument("lavfi");
      ffmpeg.addArgument("-i");
      ffmpeg.addArgument("sine=frequency=440:duration=1");
      ffmpeg.addArgument("-af");
      ffmpeg.addArgument("pan=5.1|c0=c0|c1=c0|c2=c0|c3=c0|c4=c0|c5=c0");
      ffmpeg.addArgument("-c:a");
      ffmpeg.addArgument("aac");
      ffmpeg.addArgument(target.getAbsolutePath());
      ffmpeg.execute();

      // Drain, or a full pipe stops the process.
      RBufferedReader reader = new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
      while (reader.readLine() != null) {
        // discarded
      }
      assumeTrue(
          ffmpeg.getProcessExitCode() == 0 && target.exists(),
          "Could not build a 5.1 sample with this ffmpeg, skipping");
    } finally {
      ffmpeg.destroy();
    }

    MultimediaInfo info = new MultimediaObject(target).getInfo();
    assertEquals(
        6,
        info.getAudio().getChannels(),
        "A 5.1 stream is six channels. Before #45 was fixed only mono, stereo and quad were"
            + " recognised and anything else stayed at -1");
  }

  /** Anything else has to say it does not know, so the caller leaves the value unset. */
  @Test
  public void testUnrecognisedInputIsUnknown() {
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount(null));
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount(""));
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount("   "));

    // The other tokens of an audio stream line must not be mistaken for a layout.
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount("fltp"));
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount("44100 Hz"));
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount("1411 kb/s"));
    assertEquals(ChannelLayouts.UNKNOWN, ChannelLayouts.channelCount("dts (DTS) (0x0001)"));
  }
}
