package ws.schild.jave.encode.enums;

/**
 * Add VSYNC methods described in the <a href="https://ffmpeg.org/ffmpeg.html#Advanced-options">FFMPEG Documentation</a>.
 *
 */
public enum VsyncMethod {
  /**
   * Each frame is passed with its timestamp from the demuxer to the muxer.
   */
  PASSTHROUGH("passthrough"),
  /**
   * Frames will be duplicated and dropped to achieve exactly the requested constant frame rate.
   */
  CFR("cfr"),
  /**
   * Frames are passed through with their timestamp or dropped so as to prevent 2 frames from having the same timestamp.
   */
  VFR("vfr"),
  /**
   * As passthrough but destroys all timestamps, making the muxer generate fresh timestamps based on frame-rate.
   */
  DROP("drop"),
  /**
   * Chooses between CFR and VFR depending on muxer capabilities. This is the default method.
   */
  AUTO("auto");
  
  private final String methodName;
  
  private VsyncMethod(String parameter) {
    methodName = parameter;
  }
  
  public String getMethodName() {
    return methodName;
  }
}
