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

import java.io.Serializable;

/**
 * Instances of this class report informations about videos size.
 * 
 * @author Carlo Pelliccia
 */
public class VideoSize implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The video width.
	 */
	private int width;

	/**
	 * The video height.
	 */
	private int height;

	/**
	 * It builds the bean.
	 * 
	 * @param width
	 *            The video width.
	 * @param height
	 *            The video height.
	 */
	public VideoSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the video width.
	 * 
	 * @return The video width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the video height.
	 * 
	 * @return The video height.
	 */
	public int getHeight() {
		return height;
	}

	public String toString() {
		return getClass().getName() + " (width=" + width + ", height=" + height
				+ ")";
	}

}
