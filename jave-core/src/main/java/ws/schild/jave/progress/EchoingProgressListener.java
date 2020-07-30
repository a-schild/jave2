package ws.schild.jave.progress;

import java.io.PrintStream;

/**
 * Simple class to echo progress to Standard out - or any PrintStream.
 *
 * @author mressler
 */
public class EchoingProgressListener implements VideoProgressListener {

  private String prefix;
  private PrintStream out;

  public EchoingProgressListener() {
    out = System.out;
    prefix = "";
  }

  public EchoingProgressListener(String prefix) {
    this();
    this.prefix = prefix;
  }

  public EchoingProgressListener(String prefix, PrintStream out) {
    this(prefix);
    this.out = out;
  }

  @Override
  public void onBegin() {
    out.println(prefix + " Beginning");
  }

  @Override
  public void onMessage(String message) {
    out.println(prefix + " Message Received: " + message);
  }

  @Override
  public void onProgress(Double progress) {
    out.println(prefix + " Progress Notification: " + progress);
  }

  @Override
  public void onError(String message) {
    out.println(prefix + " Error Encountered: " + message);
  }

  @Override
  public void onComplete() {
    out.println(prefix + " Complete!");
  }
}
