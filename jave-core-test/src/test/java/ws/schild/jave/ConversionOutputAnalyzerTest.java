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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

/** @author a.schild */
public class ConversionOutputAnalyzerTest extends AMediaTest {

  public ConversionOutputAnalyzerTest() {
    super(null, "ConversionOutputAnalyzer");
  }

  /** Test of getFile method, of class MultimediaObject. */
  @Test
  public void testAnalyzeNewLine1() {
    System.out.println("analyzeNewLine 1");
    Path path = Paths.get(getResourceSourcePath(), "testoutput1.txt");
    ConversionOutputAnalyzer oa1 = new ConversionOutputAnalyzer(0, null);

    try (Stream<String> lines = Files.lines(path)) {
      for (String line : lines.collect(toList())) {
        oa1.analyzeNewLine(line);
      }

      assertNull(oa1.getLastWarning());
    } catch (IOException ioError) {
      System.out.println("IO error " + ioError.getMessage());
      ioError.printStackTrace();
      throw new AssertionError("IO error " + ioError.getMessage());
    } catch (EncoderException enError) {
      System.out.println("Encoder error " + enError.getMessage());
      enError.printStackTrace();
      throw new AssertionError("Encoder error " + enError.getMessage());
    }
  }

  /** Collects whatever permil values the analyzer reports. */
  private static class RecordingListener implements EncoderProgressListener {
    final List<Integer> permils = new ArrayList<>();

    @Override
    public void sourceInfo(MultimediaInfo info) {}

    @Override
    public void progress(int permil) {
      permils.add(permil);
    }

    @Override
    public void message(String message) {}
  }

  private List<Integer> permilsFor(long duration) throws EncoderException, IOException {
    Path path = Paths.get(getResourceSourcePath(), "testoutput1.txt");
    RecordingListener listener = new RecordingListener();
    ConversionOutputAnalyzer analyzer = new ConversionOutputAnalyzer(duration, listener);
    try (Stream<String> lines = Files.lines(path)) {
      for (String line : lines.collect(toList())) {
        analyzer.analyzeNewLine(line);
      }
    }
    return listener.permils;
  }

  /**
   * A source whose duration could not be read reports {@link
   * EncoderProgressListener#PROGRESS_UNKNOWN} rather than a nonsense permil.
   *
   * <p>The duration is -1 when it is unavailable, and dividing by that gave a large negative
   * number which slipped past the upper clamp and was handed to the listener as progress (#269).
   */
  @Test
  public void testProgressWithUnknownDuration() throws Exception {
    List<Integer> permils = permilsFor(-1);

    assertFalse(permils.isEmpty(), "Expected the analyzer to report progress at all");
    for (int permil : permils) {
      assertEquals(
          EncoderProgressListener.PROGRESS_UNKNOWN,
          permil,
          "Unknown duration must report PROGRESS_UNKNOWN, not a calculated value");
    }
  }

  /**
   * Concatenating several sources leaves the duration at 0, which used to divide by zero and
   * reach the listener as -1 through an overflowing cast.
   */
  @Test
  public void testProgressWithZeroDuration() throws Exception {
    for (int permil : permilsFor(0)) {
      assertEquals(EncoderProgressListener.PROGRESS_UNKNOWN, permil);
    }
  }

  /** A known duration still reports a real proportion, within 0 to 1000. */
  @Test
  public void testProgressWithKnownDuration() throws Exception {
    // The fixture ends at time=00:14:33.80, so against a one hour source that is roughly a
    // quarter of the way through.
    List<Integer> permils = permilsFor(3600 * 1000L);

    assertFalse(permils.isEmpty(), "Expected the analyzer to report progress at all");
    for (int permil : permils) {
      assertEquals(true, permil >= 0 && permil <= 1000, "Permil out of range: " + permil);
    }
    assertEquals(243, permils.get(permils.size() - 1));
  }
}
