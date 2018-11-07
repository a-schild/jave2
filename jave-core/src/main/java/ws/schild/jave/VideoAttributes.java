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
package ws.schild.jave;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Attributes controlling the video encoding process.
 *
 * @author Carlo Pelliccia
 */
public class VideoAttributes implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * This value can be setted in the codec field to perform a direct stream
     * copy, without re-encoding of the audio stream.
     */
    public static final String DIRECT_STREAM_COPY = "copy";
    /**
     * The codec name for the encoding process. If null or not specified the
     * encoder will perform a direct stream copy.
     */
    private String codec = null;
    /**
     * The the forced tag/fourcc value for the video stream.
     */
    private String tag = null;
    /**
     * The bitrate value for the encoding process. If null or not specified a
     * default value will be picked.
     */
    private Integer bitRate = null;
    /**
     * The frame rate value for the encoding process. If null or not specified a
     * default value will be picked.
     */
    private Integer frameRate = null;
    /**
     * The video size for the encoding process. If null or not specified the
     * source video size will not be modified.
     */
    private VideoSize size = null;
    
    /**
     * The audio quality value for the encoding process. If null or not specified
     * the ffmpeg default will be used
     */
    private Integer quality = null;


    private final ArrayList<VideoFilter> videoFilters = new ArrayList<>();
    /**
     * Encode the video with faststart mode, default OFF
     *
     *
     * The mov/mp4/ismv muxer supports fragmentation. Normally, a MOV/MP4 file
     * has all the metadata about all packets stored in one location (written at
     * the end of the file, it can be moved to the start for better playback by
     * adding faststart to the movflags, or using the qt-faststart tool). A
     * fragmented file consists of a number of fragments, where packets and
     * metadata about these packets are stored together. Writing a fragmented
     * file has the advantage that the file is decodable even if the writing is
     * interrupted (while a normal MOV/MP4 is undecodable if it is not properly
     * finished), and it requires less memory when writing very long files
     * (since writing normal MOV/MP4 files stores info about every single packet
     * in memory until the file is closed). The downside is that it is less
     * compatible with other applications.
     */
    private boolean faststart = false;

    /**
     * @return the x264Profile
     */
    public X264_PROFILE getX264Profile() {
        return x264Profile;
    }

    /**
     * @param x264Profile the x264Profile to set
     */
    public void setX264Profile(X264_PROFILE x264Profile) {
        this.x264Profile = x264Profile;
    }

    public enum X264_PROFILE {
        BASELINE("baseline"), MAIN("main"), HIGH("high"),
        HIGH10("high10"), HIGH422("high422"), HIGH444("high444");
        private final String modeName;

        private X264_PROFILE(String modeName) {
            this.modeName = modeName;
        }

        public String getModeName() {
            return modeName;
        }
    };

    private X264_PROFILE x264Profile = null;

    /**
     * Returns the codec name for the encoding process.
     *
     * @return The codec name for the encoding process.
     */
    String getCodec() {
        return codec;
    }

    /**
     * Sets the codec name for the encoding process. If null or not specified
     * the encoder will perform a direct stream copy.
     *
     * Be sure the supplied codec name is in the list returned by
     * {@link Encoder#getVideoEncoders()}.
     *
     * A special value can be picked from
     * {@link VideoAttributes#DIRECT_STREAM_COPY}.
     *
     * @param codec The codec name for the encoding process.
     */
    public void setCodec(String codec) {
        this.codec = codec;
    }

    /**
     * Returns the the forced tag/fourcc value for the video stream.
     *
     * @return The the forced tag/fourcc value for the video stream.
     */
    String getTag() {
        return tag;
    }

    /**
     * Sets the forced tag/fourcc value for the video stream.
     *
     * @param tag The the forced tag/fourcc value for the video stream.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Returns the bitrate value for the encoding process.
     *
     * @return The bitrate value for the encoding process.
     */
    Integer getBitRate() {
        return bitRate;
    }

    /**
     * Sets the bitrate value for the encoding process. If null or not specified
     * a default value will be picked.
     *
     * @param bitRate The bitrate value for the encoding process.
     */
    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    /**
     * Returns the frame rate value for the encoding process.
     *
     * @return The frame rate value for the encoding process.
     */
    Integer getFrameRate() {
        return frameRate;
    }

    /**
     * Sets the frame rate value for the encoding process. If null or not
     * specified a default value will be picked.
     *
     * @param frameRate The frame rate value for the encoding process.
     */
    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    /**
     * Returns the video size for the encoding process.
     *
     * @return The video size for the encoding process.
     */
    VideoSize getSize() {
        return size;
    }

    /**
     * Sets the video size for the encoding process. If null or not specified
     * the source video size will not be modified.
     *
     * @param size he video size for the encoding process.
     */
    public void setSize(VideoSize size) {
        this.size = size;
    }

    /**
     * @return the faststart
     */
    public boolean isFaststart() {
        return faststart;
    }

    public void addFilter(VideoFilter videoFilter) {
        this.videoFilters.add(videoFilter);
    }

    public ArrayList<VideoFilter> getVideoFilters() {
        return this.videoFilters;
    }

    /**
     * @param faststart the faststart to set
     */
    public void setFaststart(boolean faststart) {
        this.faststart = faststart;
    }

    /**
     * @return the quality
     */
    public Integer getQuality() {
        return quality;
    }

    /**
     * The video quality value for the encoding process. If null or not specified
     * the ffmpeg default will be used
     * 
     * @param quality the quality to set
     */
    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(codec=" + codec 
                + ", bitRate=" + bitRate + ", frameRate=" + frameRate
                + ", size=" + size +", faststart=" + faststart
                + ", quality="+quality+ ")";
    }

}
