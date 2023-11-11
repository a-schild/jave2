package ws.schild.jave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.progress.EncoderProgressAdapter;
import ws.schild.jave.progress.VideoProgressListener;
import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.utils.AutoRemoveableFile;

/**
 * A high-level class meant to perform higher level operations on video files. Will use the Encoder
 * heavily, but presents a simpler interface for someone new to JAVE. For real customization, use of
 * Encoder is encouraged.
 *
 * @author mressler
 */
public class VideoProcessor {

  private static final Logger logger = LoggerFactory.getLogger(VideoProcessor.class);

  private static boolean enabled = false;

  private Encoder encoder;
  private ProcessLocator locator;

  public VideoProcessor() {
    try {
      locator = new DefaultFFMPEGLocator();
      encoder = new Encoder(locator);
      enabled = true;
    } catch (IllegalStateException ise) {
      logger.error("Error while starting the VideoService", ise);
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Concatenate input video files to a destination file.Destination file and parent directory must
   * be writeable.
   *
   * @see <a href="https://trac.ffmpeg.org/wiki/Concatenate">FFMPEG documentation for
   *     concatenate</a>
   * @param videos The list of videos on the local filesystem that are readable by this process that
   *     will be concatenated together
   * @param destination The target file to write to. The target file must be unique to this process
   *     and writeable.
   * @param progress Track progress of processing
   * @throws FileNotFoundException If the destination cannot be created
   * @throws EncoderException error in encoding
   * @throws InputFormatException error in input arguments
   * @throws IllegalArgumentException thrown when parameters don't match
   */
  public void catClipsTogether(List<File> videos, File destination, VideoProgressListener progress)
      throws FileNotFoundException, IllegalArgumentException, InputFormatException,
          EncoderException {
    assert (enabled);
    progress.onBegin();

    try (AutoRemoveableFile mergeFile = prepareMergeInstructions(videos, destination)) {
      MultimediaObject toMerge = fromFile(mergeFile);
      // Prevent trying to determine multimedia infos from txt file
      toMerge.setReadURLOnce(true);

      EncodingAttributes attributes = new EncodingAttributes();
      attributes.setInputFormat("concat");
      attributes.setSafe(0);

      VideoAttributes videoAttributes = new VideoAttributes();
      videoAttributes.setCodec("copy");
      attributes.setVideoAttributes(videoAttributes);

      encoder.encode(toMerge, destination, attributes, new EncoderProgressAdapter(progress));
    }

    progress.onComplete();
  }

  private MultimediaObject fromFile(File source) {
    return new MultimediaObject(source, locator);
  }

  private AutoRemoveableFile prepareMergeInstructions(List<File> videos, File destination)
      throws FileNotFoundException {
    // Create the merge instruction in the same directory as the destination
    AutoRemoveableFile mergeFile =
        new AutoRemoveableFile(destination.getParentFile(), destination.getName() + ".merge.txt");

    try (PrintStream fout = new PrintStream(mergeFile)) {
      fout.println(
          videos
              .stream()
              .map(File::getAbsolutePath)
              .collect(Collectors.joining("'\nfile '", "file '", "'")));
    }

    return mergeFile;
  }
}
