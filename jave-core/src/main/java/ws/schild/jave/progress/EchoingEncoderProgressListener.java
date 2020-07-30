package ws.schild.jave.progress;

import java.io.PrintStream;

import ws.schild.jave.info.MultimediaInfo;

/**
 * A simple progress listener that will echo progress out to any PrintStream.
 *
 * @author mressler
 */
public class EchoingEncoderProgressListener implements EncoderProgressListener {

  private PrintStream out;
  private String prefix;

  public EchoingEncoderProgressListener() {
    out = System.out;
    prefix = "";
  }

  public EchoingEncoderProgressListener(String prefix) {
    this();
    this.prefix = prefix;
  }

  public EchoingEncoderProgressListener(String prefix, PrintStream out) {
    this(prefix);
    this.out = out;
  }

  @Override
  public void sourceInfo(MultimediaInfo info) {
    out.println(prefix + " source info: " + info);
  }

  @Override
  public void progress(int permil) {
    out.println(prefix + " progress: " + permil);
  }

  @Override
  public void message(String message) {
    out.println(prefix + " message: " + message);
  }
}
