package ws.schild.jave.filters;

import ws.schild.jave.filters.helpers.ForceOriginalAspectRatio;
import ws.schild.jave.info.VideoSize;

/**
 * An implementation of the scale filter as found in the <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#scale-1">FFMPEG Documentation</a>.
 */
public class ScaleFilter extends Filter {

  public ScaleFilter() {
    super("scale");
  }

  /** 
   * Scale the video to a particular size and maintain aspect ratio.
   * @param toSize What size should the video be scaled to?
   */
  public ScaleFilter(VideoSize toSize) {
    super("scale");
    addNamedArgument("w", toSize.getWidth().toString());
    addNamedArgument("h", toSize.getHeight().toString());
  }

  /** 
   * Scale the video to a particular size and maintain aspect ratio.
   * @param scaleExpression What size should the video be scaled to?
   * Can be an expression like "trunc(iw/2)*2:trunc(ih/2)*2"
   */
  public ScaleFilter(String scaleExpression) {
    super("scale");
    addOrderedArgument(scaleExpression);
  }
  
  /** 
   * Scale the video to a particular size and maintain aspect ratio.
   * @param toSize What size should the video be scaled to?
   * @param foar Should the video be increased or decreased to size?
   */
  public ScaleFilter(VideoSize toSize, ForceOriginalAspectRatio foar) {
    super("scale");
    addNamedArgument("w", toSize.getWidth().toString());
    addNamedArgument("h", toSize.getHeight().toString());
    addNamedArgument("force_original_aspect_ratio", foar.getCommandLine());
  }
}
