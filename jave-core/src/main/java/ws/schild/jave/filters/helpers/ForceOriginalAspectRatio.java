package ws.schild.jave.filters.helpers;

public enum ForceOriginalAspectRatio {
  /**
   * Scale the video as specified and disable this feature.
   */
  DISABLE,
  /**
   * The output video dimensions will automatically be decreased if needed.
   */
  DECREASE,
  /**
   * The output video dimensions will automatically be increased if needed.
   */
  INCREASE;
  
  public String getCommandLine() {
    return name().toLowerCase();
  }
}
