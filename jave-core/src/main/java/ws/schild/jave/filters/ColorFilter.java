package ws.schild.jave.filters;

import ws.schild.jave.filters.helpers.Color;
import ws.schild.jave.info.VideoSize;

/**
 * A color filter as described by the <a href=
 * "https://ffmpeg.org/ffmpeg-filters.html#allrgb_002c-allyuv_002c-color_002c-haldclutsrc_002c-nullsrc_002c-pal75bars_002c-pal100bars_002c-rgbtestsrc_002c-smptebars_002c-smptehdbars_002c-testsrc_002c-testsrc2_002c-yuvtestsrc">
 * FFMPEG Documentation</a>.
 */
public class ColorFilter extends Filter {

  /**
   * DIY constructor - add the arguments you need.
   */
  public ColorFilter() {
    super("color");
  }
  
  /**
   * Constructor with expression
   * 
   * @param expression expression to be passed as the color argument
   */
  public ColorFilter(String expression)
  {
      super("color");
      addOrderedArgument(expression);
  }
  
  /**
   * Simple constructor - make a solid color screen for some amount of time.
   * 
   * @param c The color to use.
   * @param s The size of the output video
   * @param durationSeconds The number of seconds to output the video for
   */
  public ColorFilter(Color c, VideoSize s, Double durationSeconds) {
    super("color");
    addNamedArgument("c", c.toString());
    addNamedArgument("size", s.toString());
    addNamedArgument("d", durationSeconds.toString());
  }
}
