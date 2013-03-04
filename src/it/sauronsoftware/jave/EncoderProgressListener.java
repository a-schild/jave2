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
 * Encoding progress listener interface. Instances of implementing classes could
 * be used to listen an encoding process.
 * 
 * @author Carlo Pelliccia
 */
public interface EncoderProgressListener {

	/**
	 * This method is called before the encoding process starts, reporting
	 * information about the source stream that will be decoded and re-encoded.
	 * 
	 * @param info
	 *            Informations about the source multimedia stream.
	 */
	public void sourceInfo(MultimediaInfo info);

	/**
	 * This method is called to notify a progress in the encoding process.
	 * 
	 * @param permil
	 *            A permil value representing the encoding process progress.
	 */
	public void progress(int permil);

	/**
	 * This method is called every time the encoder need to send a message
	 * (usually, a warning).
	 * 
	 * @param message
	 *            The message sent by the encoder.
	 */
	public void message(String message);

}
