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
package ws.schild.jave.progress;

import ws.schild.jave.info.MultimediaInfo;

/**
 * Encoding progress listener interface. Instances of implementing classes could be used to listen
 * an encoding process.
 *
 * @author Carlo Pelliccia
 */
public interface EncoderProgressListener {

  /**
   * Reported to {@link #progress(int)} when the progress cannot be expressed as a proportion,
   * because the duration of the work is not known in advance.
   *
   * <p>This happens when the source does not declare a duration, which is common for streams and
   * for webm files produced by browser recorders, and when several sources are concatenated, since
   * there is then no single source whose duration could be read. The encoding itself is unaffected,
   * only the ability to say how far through it is.
   */
  public static final int PROGRESS_UNKNOWN = -1;

  /**
   * This method is called before the encoding process starts, reporting information about the
   * source stream that will be decoded and re-encoded.
   *
   * @param info Informations about the source multimedia stream.
   */
  public void sourceInfo(MultimediaInfo info);

  /**
   * This method is called to notify a progress in the encoding process.
   *
   * @param permil A permil value representing the encoding process progress, 0 to 1000, or {@link
   *     #PROGRESS_UNKNOWN} when the duration of the source is not known and no proportion can be
   *     calculated. Callers that draw a progress bar should treat {@code PROGRESS_UNKNOWN} as the
   *     cue for an indeterminate one.
   */
  public void progress(int permil);

  /**
   * This method is called every time the encoder need to send a message (usually, a warning).
   *
   * @param message The message sent by the encoder.
   */
  public void message(String message);

  /**
   * This method is called once the encoding process has run to completion, after the last call to
   * {@link #progress(int)}. It is not called when the encoding fails.
   *
   * <p>It is a default method so that listeners written against earlier versions keep compiling,
   * override it when you need to be told that the work has finished.
   */
  default void done() {}
}
