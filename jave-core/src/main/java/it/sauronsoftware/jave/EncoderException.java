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
 * Encoding expection.
 *
 * @author Carlo Pelliccia
 */
public class EncoderException extends Exception {

    private static final long serialVersionUID = 1L;

    EncoderException() {
        super();
    }

    EncoderException(String message) {
        super(message);
    }

    EncoderException(Throwable cause) {
        super(cause);
    }

    EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

}
