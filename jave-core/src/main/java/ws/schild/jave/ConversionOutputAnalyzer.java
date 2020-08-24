/*
 * Copyright 2018 a.schild.
 *
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.schild.jave.progress.EncoderProgressListener;

/** @author a.schild */
public class ConversionOutputAnalyzer {
  private static final Logger LOG = LoggerFactory.getLogger(ConversionOutputAnalyzer.class);

  /** This regexp is used to parse the ffmpeg output about the ongoing encoding process. */
  private static final Pattern PROGRESS_INFO_PATTERN =
      Pattern.compile("\\s*(\\w+)\\s*=\\s*(\\S+)\\s*", Pattern.CASE_INSENSITIVE);

  private final EncoderProgressListener listener;

  private final long duration;
  // Step 0 = Before input stuff
  // Step 1 = Input stuff
  // Step 2 = Stream Mapping
  // Step 3 = Output
  // Step 4 = frame=...
  private int step = 0;
  private int lineNR = 0;
  private String lastWarning = null;
  private final List<String> unhandledMessages = new LinkedList<>();

  public ConversionOutputAnalyzer(long duration, EncoderProgressListener listener) {
    this.duration = duration;
    this.listener = listener;
  }

  public void analyzeNewLine(String line) throws EncoderException {
    lineNR++;
    LOG.debug("Input Line ({}): <{}>", lineNR, line);
    if (line.startsWith("WARNING: ")) {
      if (listener != null) {
        listener.message(line);
      }
    }
    if (line.startsWith("Press [q]")) {
      // Abort messages
    } else {
      switch (step) {
        case 0:
          {
            if (line.startsWith("Input #0")) {
              step = 1;
            } else {
              // wait for Stream mapping:
            }
          }
          break;
        case 1:
          {
            if (line.startsWith("Stream mapping:")) {
              // streamMappingFound
              step = 2;
            } else if (line.startsWith("Output #0")) {
              // outputFound
              step = 2;
            } else if (!line.startsWith("  ")) {
              LOG.info("Unhandled message in step: {} Line: {} message: <{}>", step, lineNR, line);
              unhandledMessages.add(line);
            } else {
              // wait for Stream mapping:
            }
          }
          break;
        case 2:
          {
            if (line.startsWith("Output #0")) {
              // outputFound
              step = 3;
            } else if (line.startsWith("Stream mapping:")) {
              // streamMappingFound
              step = 3;
            } else if (!line.startsWith("  ")) {
              LOG.info("Unhandled message in step: {} Line: {} message: <{}>", step, lineNR, line);
              unhandledMessages.add(line);
            } else {
              // wait for Stream mapping:
            }
          }
          break;
        case 3:
          {
            if (line.startsWith("  ")) {
              // output details
            } else if (line.startsWith("video:")) {
              step = 4;
            } else if (line.startsWith("frame=")) {
              // Progressnotification video
            } else if (line.startsWith("size=")) {
              // Progressnotification audio
            } else if (line.endsWith("Queue input is backward in time")
                || line.contains(
                    "Application provided invalid, non monotonically increasing dts to muxer in stream")) {
              // Ignore these non-fatal errors, if they are fatal, the next line(s)
              // will throw the full error
              if (listener != null) {
                listener.message(line);
              }
            } else {
              LOG.info("Unhandled message in step: {} Line: {} message: <{}>", step, lineNR, line);
              unhandledMessages.add(line);
            }
          }
      }
      if (line.startsWith("frame=") || line.startsWith("size=")) {
        try {
          line = line.trim();
          if (line.length() > 0) {
            HashMap<String, String> table = parseProgressInfoLine(line);
            if (table == null) {
              if (listener != null) {
                listener.message(line);
              }
              lastWarning = line;
            } else {
              if (listener != null) {
                String time = table.get("time");
                if (time != null) {
                  String dParts[] = time.split(":");
                  // HH:MM:SS.xx

                  Double seconds = Double.parseDouble(dParts[dParts.length - 1]);
                  if (dParts.length > 1) {
                    seconds += Double.parseDouble(dParts[dParts.length - 2]) * 60;
                    if (dParts.length > 2) {
                      seconds += Double.parseDouble(dParts[dParts.length - 3]) * 60 * 60;
                    }
                  }

                  int perm = (int) Math.round((seconds * 1000L * 1000L) / (double) duration);
                  if (perm > 1000) {
                    perm = 1000;
                  }
                  listener.progress(perm);
                }
              }
              lastWarning = null;
            }
          }
        } catch (Exception ex) {
          LOG.warn("Error in progress parsing for line: {}", line);
        }
      }
    }
  }

  public String getLastWarning() {
    return lastWarning;
  }

  /**
   * Private utility. Parse a line and try to match its contents against the {@link
   * Encoder#PROGRESS_INFO_PATTERN} pattern. It the line can be parsed, it returns a hashtable with
   * progress informations, otherwise it returns null.
   *
   * @param line The line from the ffmpeg output.
   * @return A hashtable with the value reported in the line, or null if the given line can not be
   *     parsed.
   */
  private HashMap<String, String> parseProgressInfoLine(String line) {
    HashMap<String, String> table = null;
    Matcher m = PROGRESS_INFO_PATTERN.matcher(line);
    while (m.find()) {
      if (table == null) {
        table = new HashMap<>();
      }
      String key = m.group(1);
      String value = m.group(2);
      table.put(key, value);
    }
    return table;
  }

  /** @return the unhandledMessages */
  public List<String> getUnhandledMessages() {
    return unhandledMessages;
  }
}
