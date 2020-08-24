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

import com.google.common.io.LineReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/** @author a.schild */
public class ConversionOutputAnalyzerTest extends AMediaTest {

  public ConversionOutputAnalyzerTest() {
    super(null, "ConversionOutputAnalyzer");
  }

  /** Test of getFile method, of class MultimediaObject. */
  @Test
  public void testAnalyzeNewLine1() {
    System.out.println("analyzeNewLine 1");
    File file = new File(getResourceSourcePath(), "testoutput1.txt");
    ConversionOutputAnalyzer oa1 = new ConversionOutputAnalyzer(0, null);

    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader streamReader = new InputStreamReader(fis, "UTF-8");
      LineReader reader = new LineReader(streamReader);
      String sLine = null;
      while ((sLine = reader.readLine()) != null) {
        oa1.analyzeNewLine(sLine);
      }
      String result = oa1.getLastWarning();
      String expResult = null;
      assertEquals(expResult, result);
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
