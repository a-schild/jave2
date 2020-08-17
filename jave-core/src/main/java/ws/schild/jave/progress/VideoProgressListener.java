package ws.schild.jave.progress;

/**
 * A VideoProgressListener is meant to share progress from potentially any number of
 * EncoderProgressListeners. Because it would be hard to determine the overall status by just
 * tracking successive progress from ffmpeg, an onBbegin and onComplete have been added.
 *
 * @author mressler
 */
public interface VideoProgressListener {

  /** It has begun! */
  public void onBegin();

  /**
   * Any messages that arise during the activity.
   *
   * @param message Whatever the process reported out.
   */
  public void onMessage(String message);

  /**
   * Meaningful progress has been made.
   *
   * @param progress Current percentage complete. (0-1)
   */
  public void onProgress(Double progress);

  /**
   * An error has occurred!
   *
   * @param message The error message reported by the process.
   */
  public void onError(String message);

  /** It has ended! */
  public void onComplete();
}
