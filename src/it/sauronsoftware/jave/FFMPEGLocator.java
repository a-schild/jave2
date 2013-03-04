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
 * Abstract class whose derived concrete instances are used by {@link Encoder}
 * to locate the ffmpeg executable path.
 * 
 * @author Carlo Pelliccia
 * @see Encoder
 */
public abstract class FFMPEGLocator {

	/**
	 * This method should return the path of a ffmpeg executable suitable for
	 * the current machine.
	 * 
	 * @return The path of the ffmpeg executable.
	 */
	protected abstract String getFFMPEGExecutablePath();

	/**
	 * It returns a brand new {@link FFMPEGExecutor}, ready to be used in a
	 * ffmpeg call.
	 * 
	 * @return A newly instanced {@link FFMPEGExecutor}, using this locator to
	 *         call the ffmpeg executable.
	 */
	FFMPEGExecutor createExecutor() {
		return new FFMPEGExecutor(getFFMPEGExecutablePath());
	}

}
