/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 *
 * Copyright (C) 2008-2009 Carlo Pelliccia (www.sauronsoftware.it)
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
package ws.schild.jave.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * A package-private utility extending java.io.BufferedReader. If a line read with {@link
 * RBufferedReader#readLine()} is not useful for the calling code, it can be re-inserted in the
 * stream. The same line will be returned again at the next readLine() call.
 *
 * @author Carlo Pelliccia
 */
public class RBufferedReader extends BufferedReader {

  /** Re-inserted lines buffer. */
  private final ArrayList<String> lines = new ArrayList<>();

  /**
   * It builds the reader.
   *
   * @param in The underlying reader.
   */
  public RBufferedReader(Reader in) {
    super(in);
  }

  /** It returns the next line in the stream.
     * @throws java.io.IOException on IO errors */
  @Override
  public String readLine() throws IOException {
    if (lines.size() > 0) {
      return lines.remove(0);
    } else {
      return super.readLine();
    }
  }

  /**
   * Reinserts a line in the stream. The line will be returned at the next {@link
   * RBufferedReader#readLine()} call.
   *
   * @param line The line.
   */
  public void reinsertLine(String line) {
    lines.add(0, line);
  }
}
