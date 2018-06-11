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
package it.sauronsoftware.jave;

/**
 * Instances of this class report informations about a decoded multimedia file.
 * 
 * @author Carlo Pelliccia
 */
public class MultimediaInfo {

	/**
	 * The multimedia file format name.
	 */
	private String format = null;

	/**
	 * The stream duration in millis. If less than 0 this information is not
	 * available.
	 */
	private long duration = -1;

	/**
	 * A set of audio-specific informations. If null, there's no audio stream in
	 * the multimedia file.
	 */
	private AudioInfo audio = null;

	/**
	 * A set of video-specific informations. If null, there's no video stream in
	 * the multimedia file.
	 */
	private VideoInfo video = null;

	/**
	 * Returns the multimedia file format name.
	 * 
	 * @return The multimedia file format name.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the multimedia file format name.
	 * 
	 * @param format
	 *            The multimedia file format name.
	 */
	void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Returns the stream duration in millis. If less than 0 this information is
	 * not available.
	 * 
	 * @return The stream duration in millis. If less than 0 this information is
	 *         not available.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Sets the stream duration in millis.
	 * 
	 * @param duration
	 *            The stream duration in millis.
	 */
	void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * Returns a set of audio-specific informations. If null, there's no audio
	 * stream in the multimedia file.
	 * 
	 * @return A set of audio-specific informations.
	 */
	public AudioInfo getAudio() {
		return audio;
	}

	/**
	 * Sets a set of audio-specific informations.
	 * 
	 * @param audio
	 *            A set of audio-specific informations.
	 */
	void setAudio(AudioInfo audio) {
		this.audio = audio;
	}

	/**
	 * Returns a set of video-specific informations. If null, there's no video
	 * stream in the multimedia file.
	 * 
	 * @return A set of audio-specific informations.
	 */
	public VideoInfo getVideo() {
		return video;
	}

	/**
	 * Sets a set of video-specific informations.
	 * 
	 * @param video
	 *            A set of video-specific informations.
	 */
	void setVideo(VideoInfo video) {
		this.video = video;
	}

        @Override
	public String toString() {
		return getClass().getName() + " (format=" + format + ", duration="
				+ duration + ", video=" + video + ", audio=" + audio + ")";
	}

}
