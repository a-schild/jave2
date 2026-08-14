/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 *
 * Copyright (C) 2018- Andre Schild
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;

import org.junit.jupiter.api.Test;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;

/** @author a.schild */
public class MultimediaInfoTest extends AMediaTest {

  public MultimediaInfoTest() {
    super(null, "MultimediaInfoTest");
  }

  /**
   * Test of encode method, of class Encoder.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testVideoInfo1() throws Exception {
    System.out.println("testVideoInfo1");

    File source = new File(getResourceSourcePath(), "dance1.avi");
    MultimediaObject mo = new MultimediaObject(source);
    MultimediaInfo mi= mo.getInfo();
    AudioInfo ai= mi.getAudio();
    VideoInfo vi= mi.getVideo();
    assertNull(ai, "AudioInfo should be missing");
    assertNotNull(vi, "VideoInfo is missing");
  }
}
