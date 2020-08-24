package ws.schild.jave.process.ffmpeg;

import java.util.stream.Stream;

import ws.schild.jave.process.ProcessWrapper;

/**
 * The standard FFMPEGProcess - enhances the ProcessWrapper by always suppressing the FFMPEG banner.
 *
 * @author mressler
 */
public class FFMPEGProcess extends ProcessWrapper {

  public FFMPEGProcess(String ffmpegExecutablePath) {
    super(ffmpegExecutablePath);
  }

  @Override
  protected Stream<String> enhanceArguments(Stream<String> execArgs) {
    return Stream.concat(execArgs, Stream.of("-hide_banner"));
  }
}
