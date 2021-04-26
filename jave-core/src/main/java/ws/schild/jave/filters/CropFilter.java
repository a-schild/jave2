package ws.schild.jave.filters;

import ws.schild.jave.filters.helpers.Color;
import ws.schild.jave.info.VideoSize;

/**
 * A crop filter as described by the <a href=
 * "https://ffmpeg.org/ffmpeg-filters.html#crop">
 * FFMPEG Documentation</a>.
 */
public class CropFilter extends Filter {

  /**
   * Crop filter
   */
  public CropFilter() {
    super("crop");
  }
  
  /**
   * Simple constructor - crop input stream to given w/h//x/y
   * 
   * @param width
   * @param height
   * @param posX
   * @param posY
   */
  public CropFilter(int width, int height, int posX, int posY) {
    super("crop");
    addNamedArgument("w", Integer.toString(width));
    addNamedArgument("h", Integer.toString(height));
    addNamedArgument("x", Integer.toString(posX));
    addNamedArgument("y", Integer.toString(posY));
  }

  /**
   * Simple constructor - crop input stream with given expression
   * For example: in_w/2:in_h/2:in_w/2:in_h/2 for bottom right quarter
   * 
   * @param cropExpression string expression
   */
  public CropFilter(String cropExpression) {
    super("crop");
    addOrderedArgument(cropExpression);
  }
}
