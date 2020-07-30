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
package ws.schild.jave.info;

/**
 * Instances of this class report informations about an audio stream that can be decoded.
 *
 * @author Carlo Pelliccia
 */
public class AudioInfo {

  /** The audio stream decoder name. */
  private String decoder;

  /** The audio stream sampling rate. If less than 0, this information is not available. */
  private int samplingRate = -1;

  /**
   * The audio stream channels number (1=mono, 2=stereo). If less than 0, this information is not
   * available.
   */
  private int channels = -1;

  /** The audio stream (average) bit rate. If less than 0, this information is not available. */
  private int bitRate = -1;

  /**
   * Returns the audio stream decoder name.
   *
   * @return The audio stream decoder name.
   */
  public String getDecoder() {
    return decoder;
  }

  /**
   * Sets the audio stream decoder name.
   *
   * @param format The audio stream format name.
   * @return this instance
   */
  public AudioInfo setDecoder(String format) {
    this.decoder = format;
    return this;
  }

  /**
   * Returns the audio stream sampling rate. If less than 0, this information is not available.
   *
   * @return The audio stream sampling rate.
   */
  public int getSamplingRate() {
    return samplingRate;
  }

  /**
   * Sets the audio stream sampling rate.
   *
   * @param samplingRate The audio stream sampling rate.
   * @return this instance
   */
  public AudioInfo setSamplingRate(int samplingRate) {
    this.samplingRate = samplingRate;
    return this;
  }

  /**
   * Returns the audio stream channels number (1=mono, 2=stereo). If less than 0, this information
   * is not available.
   *
   * @return the channels The audio stream channels number (1=mono, 2=stereo).
   */
  public int getChannels() {
    return channels;
  }

  /**
   * Sets the audio stream channels number (1=mono, 2=stereo).
   *
   * @param channels The audio stream channels number (1=mono, 2=stereo).
   * @return this instance
   */
  public AudioInfo setChannels(int channels) {
    this.channels = channels;
    return this;
  }

  /**
   * Returns the audio stream (average) bit rate. If less than 0, this information is not available.
   *
   * @return The audio stream (average) bit rate.
   */
  public int getBitRate() {
    return bitRate;
  }

  /**
   * Sets the audio stream (average) bit rate.
   *
   * @param bitRate The audio stream (average) bit rate.
   * @return this instance
   */
  public AudioInfo setBitRate(int bitRate) {
    this.bitRate = bitRate;
    return this;
  }

  @Override
  public String toString() {
    return getClass().getName()
        + " (decoder="
        + decoder
        + ", samplingRate="
        + samplingRate
        + ", channels="
        + channels
        + ", bitRate="
        + bitRate
        + ")";
  }
}
