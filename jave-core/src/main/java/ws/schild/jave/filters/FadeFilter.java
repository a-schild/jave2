package ws.schild.jave.filters;

import ws.schild.jave.filters.helpers.FadeDirection;

/**
 * An implementation of the fade filter as found in the <a href= "https://ffmpeg.org/ffmpeg-filters.html#fade"> FFMPEG Documentation</a>.
 */
public class FadeFilter extends Filter {
  
  public FadeFilter(FadeDirection dir, Double startTimeSeconds, Double durationSeconds) {
    super("fade");
    addNamedArgument("type", dir.toString());
    addNamedArgument("start_time", startTimeSeconds.toString());
    addNamedArgument("duration", durationSeconds.toString());
  }
  
}
