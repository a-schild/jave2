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
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

/** @author a.schild */
public class DefaultFFMPEGLocatorTest {

  public DefaultFFMPEGLocatorTest() {}

  @Test
  public void testFindExecutable() throws IOException {
    // We first remove any old executables, to make sure the copy/deploy works
    Path dirFolder = Paths.get(System.getProperty("java.io.tmpdir"), "jave/");
    if (Files.isDirectory(dirFolder)) {
      try (Stream<Path> files = Files.list(dirFolder)) {
        for (Path filePath : files.collect(toList())) {
          Files.delete(filePath);
        }

        Files.delete(dirFolder);
      }
    }

    DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
    String exePath = locator.getExecutablePath();
    assertNotNull(exePath, "Native component not found");
  }
}
