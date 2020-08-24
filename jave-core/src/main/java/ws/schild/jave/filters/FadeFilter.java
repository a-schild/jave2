package ws.schild.jave.filters;

import ws.schild.jave.filters.helpers.FadeDirection;

/**
 * An implementation of the fade filter as found in the <a href= "https://ffmpeg.org/ffmpeg-filters.html#fade"> FFMPEG Documentation</a>.
 */
public class FadeFilter extends Filter {
  
  public FadeFilter() {
    super("fade");
  }
  
  /**
   * Standard usage - fase in or out at some time for some duration.
   * @param dir In or Out.
   * @param startTimeSeconds When to start the fade.
   * @param durationSeconds How long to fade in or out.
   */
  public FadeFilter(FadeDirection dir, Double startTimeSeconds, Double durationSeconds) {
    super("fade");
    addNamedArgument("type", dir.toString());
    addNamedArgument("start_time", startTimeSeconds.toString());
    addNamedArgument("duration", durationSeconds.toString());
  }
  
}
