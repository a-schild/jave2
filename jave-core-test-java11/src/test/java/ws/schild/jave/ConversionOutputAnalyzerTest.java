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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * @author a.schild
 */
public class ConversionOutputAnalyzerTest {

  public ConversionOutputAnalyzerTest() {
  }

  /**
   * Test of getFile method, of class MultimediaObject.
   */
  @Test
  public void testAnalyzeNewLine1() {
    System.out.println("analyzeNewLine 1");
    Path path = Paths.get("src/test/resources/testoutput1.txt");
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
}
