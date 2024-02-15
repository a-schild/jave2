package ws.schild.jave.progress;

import ws.schild.jave.info.MultimediaInfo;

public class EncoderProgressAdapter implements EncoderProgressListener {

  private final VideoProgressListener listener;

  public EncoderProgressAdapter(VideoProgressListener listener) {
    this.listener = listener;
  }

  @Override
  public void sourceInfo(MultimediaInfo info) {
    if (info != null) {
      listener.onMessage(info.toString());
    }
  }

  @Override
  public void progress(int permil) {
    listener.onProgress(Double.valueOf(permil) / 100);
  }

  @Override
  public void message(String message) {
    listener.onMessage(message);
  }
}
