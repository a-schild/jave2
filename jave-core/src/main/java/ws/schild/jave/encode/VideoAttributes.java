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
package ws.schild.jave.encode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import ws.schild.jave.Encoder;
import ws.schild.jave.encode.enums.TuneEnum;
import ws.schild.jave.encode.enums.VsyncMethod;
import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.VideoFilter;
import ws.schild.jave.info.VideoSize;

/**
 * Attributes controlling the video encoding process.
 *
 * @author Carlo Pelliccia
 */
public class VideoAttributes implements Serializable {

  private static final long serialVersionUID = 2L;
  /**
   * This value can be set in the codec field to perform a direct stream copy, without
   * re-encoding of the audio stream.
   */
  public static final String DIRECT_STREAM_COPY = "copy";
  /**
   * The codec name for the encoding process. If null or not specified the encoder will perform a
   * direct stream copy.
   */
  private String codec = null;
  
  /** The the forced tag/fourcc value for the video stream. */
  private String tag = null;
  /**
   * The bitrate value for the encoding process. If null or not specified a default value will be
   * picked.
   */
  private Integer bitRate = null;
  /**
   * The frame rate value for the encoding process. If null or not specified a default value will be
   * picked.
   */
  private Integer frameRate = null;
  /**
   * The video size for the encoding process. If null or not specified the source video size will
   * not be modified.
   */
  private VideoSize size = null;

  /**
   * The audio quality value for the encoding process. If null or not specified the ffmpeg default
   * will be used
   */
  private Integer quality = null;
  private String pixelFormat = null;
  private VsyncMethod vsync = null;

  private FilterGraph complexFiltergraph = null;
  private final ArrayList<VideoFilter> videoFilters = new ArrayList<>();
  /**
   * Encode the video with faststart mode, default OFF
   *
   * <p>The mov/mp4/ismv muxer supports fragmentation. Normally, a MOV/MP4 file has all the metadata
   * about all packets stored in one location (written at the end of the file, it can be moved to
   * the start for better playback by adding faststart to the movflags, or using the qt-faststart
   * tool). A fragmented file consists of a number of fragments, where packets and metadata about
   * these packets are stored together. Writing a fragmented file has the advantage that the file is
   * decodable even if the writing is interrupted (while a normal MOV/MP4 is undecodable if it is
   * not properly finished), and it requires less memory when writing very long files (since writing
   * normal MOV/MP4 files stores info about every single packet in memory until the file is closed).
   * The downside is that it is less compatible with other applications.
   */
  private boolean faststart = false;
  
  private TuneEnum tune = null;
  
  private X264_PROFILE x264Profile = null;

  /**
   * Set the quality for constant quality mode.
   * The lower the value, the better the quality and the larger the file size.
   * Reference Range:[0, 51]
   */
  private Integer crf = null;

  /**
   * This option itemizes a range of choices from ultrafast (best speed) to placebo (best quality).
   *
   * {@link ws.schild.jave.encode.enums.PresetEnum}
   */
  private String preset = null;


  /**
   * Returns the codec name for the encoding process.
   *
   * @return The codec name for the encoding process.
   */
  public Optional<String> getCodec() {
    return Optional.ofNullable(codec);
  }

  /**
   * Sets the codec name for the encoding process. If null or not specified the encoder will perform
   * a direct stream copy.
   *
   * <p>Be sure the supplied codec name is in the list returned by {@link
   * Encoder#getVideoEncoders()}.
   *
   * <p>A special value can be picked from {@link VideoAttributes#DIRECT_STREAM_COPY}.
   *
   * @param codec The codec name for the encoding process.
   * @return this instance
   */
  public VideoAttributes setCodec(String codec) {
    this.codec = codec;
    return this;
  }

  /**
   * Returns the the forced tag/fourcc value for the video stream.
   *
   * @return The the forced tag/fourcc value for the video stream.
   */
  public Optional<String> getTag() {
    return Optional.ofNullable(tag);
  }

  /**
   * Sets the forced tag/fourcc value for the video stream.
   *
   * @param tag The the forced tag/fourcc value for the video stream.
   * @return this instance
   */
  public VideoAttributes setTag(String tag) {
    this.tag = tag;
    return this;
  }

  /**
   * Returns the bitrate value for the encoding process.
   *
   * @return The bitrate value for the encoding process.
   */
  public Optional<Integer> getBitRate() {
    return Optional.ofNullable(bitRate);
  }

  /**
   * Sets the bitrate value for the encoding process. If null or not specified a default value will
   * be picked.
   *
   * @param bitRate The bitrate value for the encoding process.
   * @return this instance
   */
  public VideoAttributes setBitRate(Integer bitRate) {
    this.bitRate = bitRate;
    return this;
  }

  /**
   * Returns the frame rate value for the encoding process.
   *
   * @return The frame rate value for the encoding process.
   */
  public Optional<Integer> getFrameRate() {
    return Optional.ofNullable(frameRate);
  }

  /**
   * Sets the frame rate value for the encoding process. If null or not specified a default value
   * will be picked.
   *
   * @param frameRate The frame rate value for the encoding process.
   * @return this instance
   */
  public VideoAttributes setFrameRate(Integer frameRate) {
    this.frameRate = frameRate;
    return this;
  }

  /**
   * Returns the video size for the encoding process.
   *
   * @return The video size for the encoding process.
   */
  public Optional<VideoSize> getSize() {
    return Optional.ofNullable(size);
  }

  /**
   * Sets the video size for the encoding process. If null or not specified the source video size
   * will not be modified.
   *
   * @param size The video size for the encoding process.
   * @return this instance
   */
  public VideoAttributes setSize(VideoSize size) {
    this.size = size;
    return this;
  }

  /** @return the faststart */
  public boolean isFaststart() {
    return faststart;
  }

  public Optional<FilterGraph> getComplexFiltergraph() {
    return Optional.ofNullable(complexFiltergraph);
  }

  public VideoAttributes setComplexFiltergraph(FilterGraph complexFiltergraph) {
    this.complexFiltergraph = complexFiltergraph;
    return this;
  }

  public void addFilter(VideoFilter videoFilter) {
    this.videoFilters.add(videoFilter);
  }

  public ArrayList<VideoFilter> getVideoFilters() {
    return this.videoFilters;
  }

  /**
   * @param faststart the faststart to set
   * @return this instance
   */
  public VideoAttributes setFaststart(boolean faststart) {
    this.faststart = faststart;
    return this;
  }

  /** @return the quality */
  public Optional<Integer> getQuality() {
    return Optional.ofNullable(quality);
  }

  /**
   * The video quality value for the encoding process. If null or not specified the ffmpeg default
   * will be used
   *
   * @param quality the quality to set
   * @return this instance
   */
  public VideoAttributes setQuality(Integer quality) {
    this.quality = quality;
    return this;
  }

  public Optional<String> getPixelFormat() {
    return Optional.ofNullable(pixelFormat);
  }

  public VideoAttributes setPixelFormat(String pixelFormat) {
    this.pixelFormat = pixelFormat;
    return this;
  }

  public Optional<VsyncMethod> getVsync() {
    return Optional.ofNullable(vsync);
  }

  public VideoAttributes setVsync(VsyncMethod vsync) {
    this.vsync = vsync;
    return this;
  }

  @Override
  public String toString() {
    return getClass().getName()
        + "(codec="
        + codec
        + ", bitRate="
        + bitRate
        + ", frameRate="
        + frameRate
        + ", size="
        + size
        + ", faststart="
        + faststart
        + ", quality="
        + quality
        + ")";
  }
  /** @return the Tune value */
  public Optional<TuneEnum> getTune() {
    return Optional.ofNullable(tune);
  }
  /**
   * @param tune the TuneEnum to set
   * @return this instance
   */
  public VideoAttributes setTune(TuneEnum tune){
    this.tune = tune;
    return this;
  }
  /** @return the x264Profile */
  public Optional<X264_PROFILE> getX264Profile() {
    return Optional.ofNullable(x264Profile);
  }

  /**
   * @param x264Profile the x264Profile to set
   * @return this instance
   */
  public VideoAttributes setX264Profile(X264_PROFILE x264Profile) {
    this.x264Profile = x264Profile;
    return this;
  }

  /**
   * Set the quality for constant quality mode.
   *
   * @param crf the crf to set
   * @return this instance
   */
  public VideoAttributes setCrf(Integer crf) {
    this.crf = crf;
    return this;
  }

  /**
   * Get the quality for constant quality mode.
   *
   * @return the crf
   */
  public Optional<Integer> getCrf() {
    return Optional.ofNullable(crf);
  }

  /**
   * set the quality from {@link ws.schild.jave.encode.enums.PresetEnum}
   * @param preset
   * @return this instance
   */
  public VideoAttributes setPreset(String preset) {
    this.preset = preset;
    return this;
  }

  /**
   * Get the preset.
   *
   * @return the preset
   */
  public Optional<String> getPreset() {
    return Optional.ofNullable(preset);
  }

}
