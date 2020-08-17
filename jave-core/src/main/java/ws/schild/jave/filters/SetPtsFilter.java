package ws.schild.jave.filters;

/**
 * An implementation of the setpts filter as specified by <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#setpts_002c-asetpts">FFMPEG Documentation</a>.
 *
 * @author mressler
 */
public class SetPtsFilter extends Filter {

  /** Create a setpts filter that resets the presentation timestamp to zero */
  public SetPtsFilter() {
    super("setpts");
    addOrderedArgument("PTS-STARTPTS");
  }
}
