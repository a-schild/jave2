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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Turns the channel layout that ffmpeg prints for an audio stream into a number of channels.
 *
 * <p>ffmpeg describes a layout by name, {@code stereo}, {@code 5.1(side)}, {@code 7.1.4}, and falls
 * back to {@code 6 channels} when the layout is not one it has a name for. The full set of names is
 * available from {@code ffmpeg -layouts}.
 *
 * <p>Most of the names carry their own answer: the numeric ones are groups of channels that add up,
 * so {@code 5.1} is six and {@code 7.1.4} is twelve, which is worked out arithmetically rather than
 * tabulated. Only the names that say nothing about their size, {@code quad}, {@code hexagonal},
 * {@code binaural} and the rest, need to be listed, so a layout ffmpeg gains later is understood
 * without a change here as long as it is named in the usual way.
 */
public class ChannelLayouts {

  /** Returned when the layout could not be understood. */
  public static final int UNKNOWN = -1;

  /** Layouts whose name does not say how many channels they hold. */
  private static final Map<String, Integer> NAMED;

  static {
    Map<String, Integer> named = new HashMap<>();
    named.put("mono", 1);
    named.put("stereo", 2);
    named.put("downmix", 2);
    named.put("binaural", 2);
    named.put("quad", 4);
    named.put("hexagonal", 6);
    named.put("octagonal", 8);
    named.put("cube", 8);
    named.put("hexadecagonal", 16);
    NAMED = Collections.unmodifiableMap(named);
  }

  /** {@code 5.1}, {@code 7.1.4}, {@code 22.2}: groups of channels that add up. */
  private static final Pattern NUMERIC = Pattern.compile("\\d+(?:\\.\\d+)*");

  /** ffmpeg's fallback when it has no name for the layout, {@code 6 channels}. */
  private static final Pattern BARE_COUNT =
      Pattern.compile("(\\d+)\\s+channels?", Pattern.CASE_INSENSITIVE);

  private ChannelLayouts() {}

  /**
   * Works out how many channels a layout holds.
   *
   * @param layout The layout as ffmpeg printed it, such as {@code stereo}, {@code 5.1(side)} or
   *     {@code 6 channels}. A qualifier in brackets does not change the count and is ignored.
   * @return The number of channels, or {@link #UNKNOWN} when the layout was not understood.
   */
  public static int channelCount(String layout) {
    if (layout == null) {
      return UNKNOWN;
    }
    String name = layout.trim().toLowerCase(Locale.ROOT);
    if (name.isEmpty()) {
      return UNKNOWN;
    }

    // "6 channels", and "6 channels (FL+FR+...)", which ffmpeg uses when it has no name.
    Matcher bare = BARE_COUNT.matcher(name);
    if (bare.lookingAt()) {
      return positiveOrUnknown(Integer.parseInt(bare.group(1)));
    }

    /*
     * "5.1(side)" and "5.1" are the same six channels, the qualifier only says which six, so it
     * plays no part in the count. The same goes for "quad(side)" and "6.1(back)".
     */
    int bracket = name.indexOf('(');
    if (bracket > 0) {
      name = name.substring(0, bracket).trim();
    }

    Integer known = NAMED.get(name);
    if (known != null) {
      return known;
    }

    if (NUMERIC.matcher(name).matches()) {
      int total = 0;
      for (String group : name.split("\\.")) {
        total += Integer.parseInt(group);
      }
      return positiveOrUnknown(total);
    }

    return UNKNOWN;
  }

  private static int positiveOrUnknown(int count) {
    return count > 0 ? count : UNKNOWN;
  }
}
