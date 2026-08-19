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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.utils.RBufferedReader;

/**
 * Checks which ffmpeg the package for this platform actually carries.
 *
 * <p>Nothing else in the project records that. The version lives inside the binaries and in prose
 * in the changelog, so a platform can fall behind without anything noticing, which is how the 32
 * bit arm package sat on 4.4 while every other package moved to 4.4.1 and then past it.
 *
 * <p>The expectation is one constant, with an entry per platform that is knowingly behind. A
 * platform that falls behind by accident therefore fails here, and one that is behind on purpose
 * has to be written down to pass.
 */
public class BundledFFmpegVersionTest {

  /** What every package is expected to carry unless it appears in {@link #KNOWINGLY_BEHIND}. */
  private static final String EXPECTED_VERSION = "9.0";

  /**
   * Packages not yet on {@link #EXPECTED_VERSION}, keyed by {@code os.arch}, with the version they
   * do carry. Remove an entry as soon as its binary is replaced.
   */
  private static final Map<String, String> KNOWINGLY_BEHIND = new HashMap<>();

  static {
    // Empty, and worth keeping that way. 32 bit arm was the last one behind, on 4.4,
    // and is now built alongside the others.
  }

  /**
   * Test that the bundled ffmpeg is the version this project means to ship.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testBundledFFmpegVersion() throws Exception {
    String arch = System.getProperty("os.arch");
    String expected = KNOWINGLY_BEHIND.getOrDefault(arch, EXPECTED_VERSION);

    String reported = readBundledFFmpegVersion();
    System.out.println("bundled ffmpeg on " + arch + ": " + reported);

    assertNotNull(reported, "Could not read a version out of the bundled ffmpeg");
    assertTrue(
        reported.startsWith(expected),
        "The ffmpeg bundled for "
            + arch
            + " reports "
            + reported
            + " where "
            + expected
            + " was expected. Either the binary was replaced without updating"
            + " BundledFFmpegVersionTest, or this platform has fallen behind the others.");
  }

  /**
   * Runs the bundled executable and picks the version out of its first line, which reads like
   * {@code ffmpeg version 9.0.1 Copyright (c) ...}. Builders append their own name to it, so the
   * token can be anything from {@code 9.0.1} to {@code 9.0.1-essentials_build-www.gyan.dev}.
   *
   * @return The version token, or null when the output did not look as expected.
   * @throws Exception If the executable cannot be run.
   */
  private String readBundledFFmpegVersion() throws Exception {
    ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
    ffmpeg.addArgument("-version");
    try {
      ffmpeg.execute();
      RBufferedReader reader =
          new RBufferedReader(new InputStreamReader(ffmpeg.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        String marker = "ffmpeg version ";
        int at = line.indexOf(marker);
        if (at >= 0) {
          String rest = line.substring(at + marker.length()).trim();
          int space = rest.indexOf(' ');
          return space > 0 ? rest.substring(0, space) : rest;
        }
      }
      return null;
    } finally {
      ffmpeg.destroy();
    }
  }
}
