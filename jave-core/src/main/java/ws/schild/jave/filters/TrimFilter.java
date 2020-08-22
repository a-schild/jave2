package ws.schild.jave.filters;

/**
 * An implementation of the overlay filter as specified by <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#trim">FFMPEG Documentation</a>
 *
 * <p>Important implementation note: Most common usage of the trim filter requires a setpts filter
 * applied immediately after in the filter chain.
 *
 * @author mressler
 */
public class TrimFilter extends Filter {  
  
  public TrimFilter() {
    super("trim");
  }
  
  public TrimFilter(Double start, Double duration) {
    super("trim");
    addNamedArgument("start", start.toString());
    addNamedArgument("duration", duration.toString());
  }

  public TrimFilter(String inputLabel, Double start, Double duration) {
    this(start, duration);
    addInputLabel(inputLabel);
  }
}
