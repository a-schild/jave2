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

import java.io.Serializable;
import java.util.Objects;

/**
 * Instances of this class report information about videos size.
 *
 * @author Carlo Pelliccia
 */
public class VideoSize implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The video width. */
  private final Integer width;

  /** The video height. */
  private final Integer height;

  /**
   * It builds the bean.
   *
   * @param width The video width.
   * @param height The video height.
   */
  public VideoSize(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  public static final VideoSize ntsc = new VideoSize(720, 480);
  public static final VideoSize pal = new VideoSize(720, 576);
  public static final VideoSize qntsc = new VideoSize(352, 240);
  public static final VideoSize qpal = new VideoSize(352, 288);
  public static final VideoSize sntsc = new VideoSize(640, 480);
  public static final VideoSize spal = new VideoSize(768, 576);
  public static final VideoSize film = new VideoSize(352, 240);
  public static final VideoSize ntsc_film = new VideoSize(352, 240);
  public static final VideoSize sqcif = new VideoSize(128, 96);
  public static final VideoSize qcif = new VideoSize(176, 144);
  public static final VideoSize cif = new VideoSize(352, 288);
  public static final VideoSize FOUR_cif = new VideoSize(704, 576);
  public static final VideoSize SIXTEEN_cif = new VideoSize(1408, 1152);
  public static final VideoSize qqvga = new VideoSize(160, 120);
  public static final VideoSize qvga = new VideoSize(320, 240);
  public static final VideoSize vga = new VideoSize(640, 480);
  public static final VideoSize svga = new VideoSize(800, 600);
  public static final VideoSize xga = new VideoSize(1024, 768);
  public static final VideoSize uxga = new VideoSize(1600, 1200);
  public static final VideoSize qxga = new VideoSize(2048, 1536);
  public static final VideoSize sxga = new VideoSize(1280, 1024);
  public static final VideoSize qsxga = new VideoSize(2560, 2048);
  public static final VideoSize hsxga = new VideoSize(5120, 4096);
  public static final VideoSize wvga = new VideoSize(852, 480);
  public static final VideoSize wxga = new VideoSize(1366, 768);
  public static final VideoSize wsxga = new VideoSize(1600, 1024);
  public static final VideoSize wuxga = new VideoSize(1920, 1200);
  public static final VideoSize woxga = new VideoSize(2560, 1600);
  public static final VideoSize wqsxga = new VideoSize(3200, 2048);
  public static final VideoSize wquxga = new VideoSize(3840, 2400);
  public static final VideoSize whsxga = new VideoSize(6400, 4096);
  public static final VideoSize whuxga = new VideoSize(7680, 4800);
  public static final VideoSize cga = new VideoSize(320, 200);
  public static final VideoSize ega = new VideoSize(640, 350);
  public static final VideoSize hd480 = new VideoSize(852, 480);
  public static final VideoSize hd720 = new VideoSize(1280, 720);
  public static final VideoSize hd1080 = new VideoSize(1920, 1080);
  public static final VideoSize TWOk = new VideoSize(2048, 1080);
  public static final VideoSize TWOkflat = new VideoSize(1998, 1080);
  public static final VideoSize TWOkscope = new VideoSize(2048, 858);
  public static final VideoSize FOURk = new VideoSize(4096, 2160);
  public static final VideoSize FOURkflat = new VideoSize(3996, 2160);
  public static final VideoSize FOURkscope = new VideoSize(4096, 1716);
  public static final VideoSize nhd = new VideoSize(640, 360);
  public static final VideoSize hqvga = new VideoSize(240, 160);
  public static final VideoSize wqvga = new VideoSize(400, 240);
  public static final VideoSize fwqvga = new VideoSize(432, 240);
  public static final VideoSize hvga = new VideoSize(480, 320);
  public static final VideoSize qhd = new VideoSize(960, 540);
  public static final VideoSize TWOkdci = new VideoSize(2048, 1080);
  public static final VideoSize FOURkdci = new VideoSize(4096, 2160);
  public static final VideoSize uhd2160 = new VideoSize(3840, 2160);
  public static final VideoSize uhd4320 = new VideoSize(7680, 4320);

  /**
   * Returns the video width.
   *
   * @return The video width.
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * Returns the video height.
   *
   * @return The video height.
   */
  public Integer getHeight() {
    return height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VideoSize videoSize = (VideoSize) o;
    return Objects.equals(width, videoSize.width) && Objects.equals(height, videoSize.height);
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height);
  }

  @Override
  public String toString() {
    return getClass().getName() + " (width=" + width + ", height=" + height + ")";
  }

  public String asEncoderArgument() {
    return getWidth() + "x" + getHeight();
  }
  
  public String aspectRatioExpression() {
    return "(" + getWidth() + "/" + getHeight() + ")";
  }
}
