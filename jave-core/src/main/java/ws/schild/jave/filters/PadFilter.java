package ws.schild.jave.filters;

import ws.schild.jave.info.VideoSize;

public class PadFilter extends Filter {

  public PadFilter() {
    super("pad");
  }

  /**
   * Uses the <a href="https://ffmpeg.org/ffmpeg-filters.html#pad-1">pad</a> filter to pad the
   * source image to the same aspect ratio as {@code aspectRatio}.
   *
   * @param aspectRatio A {@link VideoSize} that represents the desired resulting aspect ratio.
   */
  public PadFilter(VideoSize aspectRatio) {
    super("pad");
    addNamedArgument("w", "ih*" + aspectRatio.aspectRatioExpression());
    addNamedArgument("h", "ih");
    addNamedArgument("x", "(ow-iw)/2");
    addNamedArgument("y", "(oh-ih)/2");
  }
}
