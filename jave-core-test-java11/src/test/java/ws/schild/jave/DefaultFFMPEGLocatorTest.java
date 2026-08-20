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

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

/**
 * @author a.schild
 */
public class DefaultFFMPEGLocatorTest {

  public DefaultFFMPEGLocatorTest() {
  }

  @Test
  public void testFindExecutable() throws IOException {
    /*
     * Clear out any old executables first, so that the locator has to extract one rather than
     * find what an earlier test left behind.
     *
     * This directory is shared with every other test in the build, and windows refuses to delete
     * an executable that a process has just finished with, so a delete here can fail through no
     * fault of the locator. Failing to clean up is therefore tolerated, and the assertions below
     * check the result either way.
     */
    Path dirFolder = Paths.get(System.getProperty("java.io.tmpdir"), "jave/");
    if (Files.isDirectory(dirFolder)) {
      try (Stream<Path> files = Files.list(dirFolder)) {
        for (Path filePath : files.collect(toList())) {
          deleteIfPossible(filePath);
        }
      }
      deleteIfPossible(dirFolder);
    }

    DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
    String exePath = locator.getExecutablePath();
    assertNotNull(exePath, "Native component not found");

    File executable = new File(exePath);
    assertTrue(executable.isFile(), "The locator returned a path that is not a file: " + exePath);
    assertTrue(executable.length() > 0, "The extracted executable is empty: " + exePath);
    assertTrue(executable.canExecute(), "The extracted executable is not executable: " + exePath);
  }

  /** Windows holds on to executables for a while after use, and that is not this test's problem. */
  private void deleteIfPossible(Path path) {
    try {
      Files.deleteIfExists(path);
    } catch (IOException stillInUse) {
      System.out.println("Could not remove " + path + ", carrying on: " + stillInUse.getMessage());
    }
  }
}
