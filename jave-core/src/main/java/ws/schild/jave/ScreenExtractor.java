package ws.schild.jave;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.utils.RBufferedReader;
import ws.schild.jave.utils.Utils;

public class ScreenExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(ScreenExtractor.class);

  /** The locator of the ffmpeg executable used by this extractor. */
  private final ProcessLocator locator;

  private int numberOfScreens;

  /**
   * It builds an extractor using a {@link ws.schild.jave.process.ffmpeg.FFMPEGProcess} instance to
   * locate the ffmpeg executable to use.
   */
  public ScreenExtractor() {
    this.locator = new DefaultFFMPEGLocator();
  }

  /** @return The number of screens found */
  public int getNumberOfScreens() {
    return numberOfScreens;
  }

  /**
   * It builds an extractor with a custom {@link ws.schild.jave.process.ffmpeg.FFMPEGProcess}.
   *
   * @param locator The locator picking up the ffmpeg executable used by the extractor.
   */
  public ScreenExtractor(ProcessLocator locator) {
    this.locator = locator;
  }

  /**
   * Generates screenshots from source video.
   *
   * @param multimediaObject Source MultimediaObject @see MultimediaObject
   * @param width Output width, pass -1 to use video width and height
   * @param height Output height (Ignored when width = -1)
   * @param seconds Interval in seconds between screens
   * @param outputDir Destination of output images
   * @param fileNamePrefix Name all thumbnails will start with
   * @param extension Image extension for output (jpg, png, etc)
   * @param quality The range is between 1-31 with 31 being the worst quality
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void render(
      MultimediaObject multimediaObject,
      int width,
      int height,
      int seconds,
      File outputDir,
      String fileNamePrefix,
      String extension,
      int quality)
      throws InputFormatException, EncoderException {
    String inputSource = multimediaObject.toString();
    try {
      if (!outputDir.exists()) {
        if (!outputDir.mkdirs()) {
          LOG.debug("Failed to create destination folder");
          throw new SecurityException();
        }
      }
      if (!multimediaObject.isURL() && !multimediaObject.getFile().canRead()) {
        LOG.debug("Failed to open input file");
        throw new SecurityException();
      }
    } catch (SecurityException e) {
      LOG.debug("Access denied checking destination folder", e);
    }

    MultimediaInfo multimediaInfo = multimediaObject.getInfo();
    numberOfScreens = Math.round(((float) multimediaInfo.getDuration()) / 1000.0f / seconds);

    ProcessWrapper ffmpeg = this.locator.createExecutor();
    ffmpeg.addArgument("-i");
    ffmpeg.addArgument(inputSource);
    ffmpeg.addArgument("-f");
    ffmpeg.addArgument("image2");
    ffmpeg.addArgument("-vf");
    ffmpeg.addArgument(String.format("fps=fps=1/%s", String.valueOf(seconds)));
    if (width != -1) {
      ffmpeg.addArgument("-s");
      ffmpeg.addArgument(String.format("%sx%s", String.valueOf(width), String.valueOf(height)));
    }
    ffmpeg.addArgument("-qscale");
    ffmpeg.addArgument(String.valueOf(quality));
    ffmpeg.addArgument(
        String.format(
            "%s%s%s-%%04d.%s",
            outputDir.getAbsolutePath(), File.separator, fileNamePrefix, extension));

    try {
      ffmpeg.execute();
    } catch (IOException e) {
      throw new EncoderException(e);
    }
    try {
      RBufferedReader reader = new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
      int lineNR = 0;
      String line;
      while ((line = reader.readLine()) != null) {
        lineNR++;
        LOG.debug("Input Line ({}): {}", lineNR, line);
        // TODO: Implement additional input stream parsing
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    } finally {
      ffmpeg.destroy();
    }
  }

  /**
   * Generate a single screenshot from source video.
   *
   * @param multimediaObject Source MultimediaObject @see MultimediaObject
   * @param width Output width, pass -1 to use video width and height
   * @param height Output height (Ignored when width = -1)
   * @param seconds Interval in seconds between screens
   * @param outputDir Destination folder of output image
   * @param quality The range is between 1-31 with 31 being the worst quality
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void render(
      MultimediaObject multimediaObject,
      int width,
      int height,
      int seconds,
      File outputDir,
      int quality)
      throws EncoderException {
    String inputSource = multimediaObject.toString();
    outputDir = outputDir.getAbsoluteFile();
    outputDir.getParentFile().mkdirs();
    try {
      if (!multimediaObject.isURL() && !multimediaObject.getFile().canRead()) {
        LOG.debug("Failed to open input file");
        throw new SecurityException();
      }
    } catch (SecurityException e) {
      LOG.debug("Access denied checking destination folder", e);
    }

    MultimediaInfo multimediaInfo = multimediaObject.getInfo();
    int duration = (int) (multimediaInfo.getDuration() / 1000);
    numberOfScreens = seconds <= duration ? 1 : 0;

    ProcessWrapper ffmpeg = this.locator.createExecutor();
    ffmpeg.addArgument("-i");
    ffmpeg.addArgument(inputSource);
    ffmpeg.addArgument("-f");
    ffmpeg.addArgument("image2");
    ffmpeg.addArgument("-vframes");
    ffmpeg.addArgument("1");
    ffmpeg.addArgument("-ss");
    ffmpeg.addArgument(Utils.buildTimeDuration(seconds * 1000));
    if (width != -1) {
      ffmpeg.addArgument("-s");
      ffmpeg.addArgument(String.format("%sx%s", String.valueOf(width), String.valueOf(height)));
    }
    ffmpeg.addArgument("-qscale");
    ffmpeg.addArgument(String.valueOf(quality));
    ffmpeg.addArgument(outputDir.getAbsolutePath());

    try {
      ffmpeg.execute();
    } catch (IOException e) {
      throw new EncoderException(e);
    }
    try {
      RBufferedReader reader = new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
      int lineNR = 0;
      String line;
      while ((line = reader.readLine()) != null) {
        lineNR++;
        LOG.debug("Input Line ({}): {}", lineNR, line);
        // TODO: Implement additional input stream parsing
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    } finally {
      ffmpeg.destroy();
    }
  }

  /**
   * Generate exactly <b>one</b> screenshot from source video
   *
   * @param multimediaObject Source MultimediaObject @see MultimediaObject
   * @param width Output width, pass -1 to use video width and height
   * @param height Output height (Ignored when width = -1)
   * @param millis At which second in the video should the screenshot be made
   * @param outputFile Outputfile
   * @param quality The range is between 1-31 with 31 being the worst quality
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void renderOneImage(
      MultimediaObject multimediaObject,
      int width,
      int height,
      long millis,
      File outputFile,
      int quality)
      throws InputFormatException, EncoderException {
    renderOneImage(multimediaObject, width, height, millis, outputFile, quality, true);
  }

  /**
   * Generate exactly <b>one</b> screenshot from source video using given seeking mode.
   *
   * @param multimediaObject Source MultimediaObject @see MultimediaObject
   * @param width Output width, pass -1 to use video width and height
   * @param height Output height (Ignored when width = -1)
   * @param millis At which second in the video should the screenshot be made
   * @param outputFile Outputfile
   * @param quality The range is between 1-31 with 31 being the worst quality
   * @param keyframesSeeking If True, it forces FFmpeg to parse an input file using keyframes, which
   *     is very fast. If False, input will be parsed frame by frame. See <a
   *     href="http://trac.ffmpeg.org/wiki/Seeking">FFmpeg Wiki: Seeking</a>
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void renderOneImage(
      MultimediaObject multimediaObject,
      int width,
      int height,
      long millis,
      File outputFile,
      int quality,
      boolean keyframesSeeking)
      throws InputFormatException, EncoderException {
    String inputSource = multimediaObject.toString();
    try {
      if (outputFile.exists()) {
        outputFile.delete();
      }
      if (!multimediaObject.isURL() && !multimediaObject.getFile().canRead()) {
        LOG.debug("Failed to open input file");
        throw new SecurityException();
      }
    } catch (SecurityException e) {
      LOG.debug("Access denied checking destination folder", e);
    }

    ProcessWrapper ffmpeg = this.locator.createExecutor();
    if (keyframesSeeking) {
      ffmpeg.addArgument("-ss");
      ffmpeg.addArgument(Utils.buildTimeDuration(millis));
    }
    ffmpeg.addArgument("-i");
    ffmpeg.addArgument(inputSource);
    if (!keyframesSeeking) {
      ffmpeg.addArgument("-ss");
      ffmpeg.addArgument(Utils.buildTimeDuration(millis));
    }
    ffmpeg.addArgument("-vframes");
    ffmpeg.addArgument("1");
    if (width != -1) {
      ffmpeg.addArgument("-s");
      ffmpeg.addArgument(String.format("%sx%s", String.valueOf(width), String.valueOf(height)));
    }

    ffmpeg.addArgument("-qscale");
    ffmpeg.addArgument(String.valueOf(quality));
    ffmpeg.addArgument(outputFile.getAbsolutePath());

    try {
      ffmpeg.execute();
    } catch (IOException e) {
      throw new EncoderException(e);
    }
    try {
      RBufferedReader reader = new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
      int lineNR = 0;
      String line;
      while ((line = reader.readLine()) != null) {
        lineNR++;
        LOG.debug("Input Line ({}): {}", lineNR, line);
        // TODO: Implement additional input stream parsing
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    } finally {
      ffmpeg.destroy();
    }
  }
}
