package ws.schild.jave.filters;

/** @author jgiotta */
public interface VideoFilter {

  /**
   * The expression to be used in the video filter argument to ffmpeg
   *
   * @return A string that will be placed in the -vf or -filter_complex option to ffmpeg.
   */
  public String getExpression();
  
}
