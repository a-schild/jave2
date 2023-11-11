package ws.schild.jave;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.utils.RBufferedReader;

/*
 * TODO: Rip out parsing logic. This shouldn't be in a POJO object. This is meant to be a data 
 * holder.
 * 
 * Also TODO: Do away with the distinction between URL and File altogether. Shouldn't this just be
 * a String anyway? We don't ever need the distinction between the two, correct?
 */
public class MultimediaObject {

  /** @param readURLOnce the readURLOnce to set */
  public void setReadURLOnce(boolean readURLOnce) {
    this.readURLOnce = readURLOnce;
  }

  private static final Logger LOG = LoggerFactory.getLogger(MultimediaObject.class);
  /** This regexp is used to parse the ffmpeg output about the size of a video stream. */
  private static final Pattern SIZE_PATTERN =
      Pattern.compile("(\\d+)x(\\d+)", Pattern.CASE_INSENSITIVE);
  /**
   * This regexp is used to parse the ffmpeg output about the frame rate value of a video stream.
   */
  private static final Pattern FRAME_RATE_PATTERN =
      Pattern.compile("([\\d.]+)\\s+(?:fps|tbr)", Pattern.CASE_INSENSITIVE);
  /** This regexp is used to parse the ffmpeg output about the bit rate value of a stream. */
  private static final Pattern BIT_RATE_PATTERN =
      Pattern.compile("(\\d+)\\s+kb/s", Pattern.CASE_INSENSITIVE);
  /** This regexp is used to parse the ffmpeg output about the sampling rate of an audio stream. */
  private static final Pattern SAMPLING_RATE_PATTERN =
      Pattern.compile("(\\d+)\\s+Hz", Pattern.CASE_INSENSITIVE);
  /**
   * This regexp is used to parse the ffmpeg output about the channels number of an audio stream.
   */
  private static final Pattern CHANNELS_PATTERN =
      Pattern.compile("(mono|stereo|quad)", Pattern.CASE_INSENSITIVE);

  /**
   * This regexp is used to parse the ffmpeg output about the bit depth of an audio stream.
   */
  private static final Pattern BIT_DEPTH_PATTERN =
          Pattern.compile("(s16|s16p|s32|fltp|dblp|s64)", Pattern.CASE_INSENSITIVE);

  /** The locator of the ffmpeg executable used by this extractor. */
  private final ProcessLocator locator;

  private File inputFile;
  private URL inputURL;
  /**
   * When true, we try to not read the source more than once.
   * One of the side effects is, that no progressbar is available
   */
  private boolean readURLOnce = false;

  /**
   * It builds an extractor using a {@link DefaultFFMPEGLocator} instance to locate the ffmpeg
   * executable to use.
   *
   * @param input Input file for creating MultimediaObject
   */
  public MultimediaObject(File input) {
    this.locator = new DefaultFFMPEGLocator();
    this.inputFile = input;
  }

  /**
   * It builds an extractor using a {@link DefaultFFMPEGLocator} instance to locate the ffmpeg
   * executable to use.
   *
   * @param input Input URL for creating MultimediaObject
   */
  public MultimediaObject(URL input) {
    this.locator = new DefaultFFMPEGLocator();
    this.inputURL = input;
  }

  /**
   * It builds an extractor using a {@link DefaultFFMPEGLocator} instance to locate the ffmpeg
   * executable to use.
   *
   * @param input Input URL for creating MultimediaObject
   * @param readURLOnce When true, we try to not read the source more than once One of the side
   *     effects is, that no progressbar is available.
   */
  public MultimediaObject(URL input, boolean readURLOnce) {
    this.locator = new DefaultFFMPEGLocator();
    this.inputURL = input;
    this.readURLOnce = readURLOnce;
  }

  /** @return file */
  public File getFile() {
    return this.inputFile;
  }

  public URL getURL() {
    return this.inputURL;
  }

  public void setFile(File file) {
    this.inputFile = file;
  }

  public void setURL(URL input) {
    this.inputURL = input;
  }

  /**
   * Check if we have a file or an URL
   *
   * @return true if this object references an URL
   */
  public boolean isURL() {
    return inputURL != null;
  }

  /**
   * It builds an extractor with a custom {@link ws.schild.jave.process.ffmpeg.FFMPEGProcess}.
   *
   * @param input Input file for creating MultimediaObject
   * @param locator The locator picking up the ffmpeg executable used by the extractor.
   */
  public MultimediaObject(File input, ProcessLocator locator) {
    this.locator = locator;
    this.inputFile = input;
  }

  /**
   * Returns a set informations about a multimedia file, if its format is supported for decoding.
   *
   * @return A set of informations about the file and its contents.
   * @throws InputFormatException If the format of the source file cannot be recognized and decoded.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public MultimediaInfo getInfo() throws InputFormatException, EncoderException {
    if (isURL() || inputFile.canRead()) {
      ProcessWrapper ffmpeg = locator.createExecutor();
      ffmpeg.addArgument("-i");
      ffmpeg.addArgument(toString());
      
      try {
        ffmpeg.execute();
      } catch (IOException e) {
        throw new EncoderException(e);
      }
      try {
        RBufferedReader reader =
            new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
        if (isURL()) {
          return parseMultimediaInfo(inputURL.toString(), reader);
        } else {
          return parseMultimediaInfo(inputFile.getAbsolutePath(), reader);
        }
      } finally {
        ffmpeg.destroy();
      }
    } else {
      throw new EncoderException("Input file not found <" + inputFile.getAbsolutePath() + ">");
    }
  }

  /**
   * Private utility. It parses the ffmpeg output, extracting informations about a source multimedia
   * file.
   *
   * @param source The source multimedia object.
   * @param reader The ffmpeg output channel.
   * @return A set of informations about the source multimedia file and its contents.
   * @throws InputFormatException If the format of the source file cannot be recognized and decoded.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  /* 
   * TODO: Refactor all parsing logic to a versioned parsing utility so we can detect FFMPEG version
   * programmatically/support multiple runtime versions and consolidate parsing in one location. 
   */
  private MultimediaInfo parseMultimediaInfo(String source, RBufferedReader reader)
      throws InputFormatException, EncoderException {
    Pattern p1 = Pattern.compile("^\\s*Input #0, (\\w+).+$\\s*", Pattern.CASE_INSENSITIVE);
    Pattern p21 = Pattern.compile("^\\s*Duration:.*$", Pattern.CASE_INSENSITIVE);
    Pattern p22 =
        Pattern.compile(
            "^\\s*Duration: (\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d\\d).*$", Pattern.CASE_INSENSITIVE);
    Pattern p3 =
        Pattern.compile(
            "^\\s*Stream #\\S+: ((?:Audio)|(?:Video)|(?:Data)): (.*)\\s*$",
            Pattern.CASE_INSENSITIVE);
	Pattern p4 = Pattern.compile("^\\s*Metadata:", Pattern.CASE_INSENSITIVE);
	Pattern p5 = Pattern.compile("^\\s*(\\w+)\\s*:\\s*(\\S+)\\s*$", Pattern.CASE_INSENSITIVE);
    MultimediaInfo info = null;
    try {
      int step = 0;
      while (true) {
        String line = reader.readLine();
        LOG.debug("Output line: {}", line);
        if (line == null) {
          break;
        }
        switch (step) {
          case 0:
            {
              String token = source + ": ";
              if (line.startsWith(token)) {
                String message = line.substring(token.length());
                throw new InputFormatException(message);
              }
              Matcher m = p1.matcher(line);
              if (m.matches()) {
                String format = m.group(1);
                info = new MultimediaInfo();
                info.setFormat(format);
                step++;
              }
              break;
            }
          case 1:
            {
              Matcher m1 = p21.matcher(line);
              Matcher m2 = p22.matcher(line);
              if (m1.matches()) {
                if (m2.matches()) {
                  long hours = Integer.parseInt(m2.group(1));
                  long minutes = Integer.parseInt(m2.group(2));
                  long seconds = Integer.parseInt(m2.group(3));
                  long dec = Integer.parseInt(m2.group(4));
                  long duration =
                      (dec * 10L)
                          + (seconds * 1000L)
                          + (minutes * 60L * 1000L)
                          + (hours * 60L * 60L * 1000L);
                  info.setDuration(duration);
                  step++;
                } else {
                  LOG.warn("Invalid duration found {}", line);
                  step++;
                  // step = 3;
                }
              } else {
					Matcher m4 = p4.matcher(line);
					if (m4.matches()) {
						line = reader.readLine();
						while (line != null && !p21.matcher(line).matches()) {
							LOG.debug("Output line: {}", line);
							Matcher m5 = p5.matcher(line);
							if (m5.matches()) {
								info.getMetadata().put(m5.group(1), m5.group(2));
							}
							line = reader.readLine();
						}
						reader.reinsertLine(line);
					}
              }
              break;
            }
          case 2:
            {
              Matcher m = p3.matcher(line);
              if (m.matches()) {
                String type = m.group(1);
                String specs = m.group(2);
                if ("Video".equalsIgnoreCase(type)) {
                  VideoInfo video = new VideoInfo();
                  StringTokenizer st = new StringTokenizer(specs, ",");
                  for (int i = 0; st.hasMoreTokens(); i++) {
                    String token = st.nextToken().trim();
                    if (i == 0) {
                      video.setDecoder(token);
                    } else {
                      boolean parsed = false;
                      // Video size.
                      Matcher m2 = SIZE_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        int width = Integer.parseInt(m2.group(1));
                        int height = Integer.parseInt(m2.group(2));
                        video.setSize(new VideoSize(width, height));
                        parsed = true;
                      }
                      // Frame rate.
                      m2 = FRAME_RATE_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        try {
                          float frameRate = Float.parseFloat(m2.group(1));
                          video.setFrameRate(frameRate);
                        } catch (NumberFormatException e) {
                          LOG.info("Invalid frame rate value: " + m2.group(1), e);
                        }
                        parsed = true;
                      }
                      // Bit rate.
                      m2 = BIT_RATE_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        int bitRate = Integer.parseInt(m2.group(1));
                        video.setBitRate(bitRate * 1000);
                        parsed = true;
                      }
                    }
                  }
                  //reading vedio metadata
                  line = reader.readLine();
                  Matcher m4 = p4.matcher(line);
                  if(m4.matches()){
                    line = reader.readLine();
                    while (line != null && p5.matcher(line).matches()) {
                      LOG.debug("Output line: {}", line);
                      Matcher m5 = p5.matcher(line);
                      if (m5.matches()) {
                        video.getMetadata().put(m5.group(1), m5.group(2));
                      }
                      line = reader.readLine();
                    }
                    reader.reinsertLine(line);
                  } else {
                    reader.reinsertLine(line);
                  }
                  info.setVideo(video);
                } else if ("Audio".equalsIgnoreCase(type)) {
                  AudioInfo audio = new AudioInfo();
                  StringTokenizer st = new StringTokenizer(specs, ",");
                  for (int i = 0; st.hasMoreTokens(); i++) {
                    String token = st.nextToken().trim();
                    if (i == 0) {
                      audio.setDecoder(token);
                    } else {
                      boolean parsed = false;
                      // Sampling rate.
                      Matcher m2 = SAMPLING_RATE_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        int samplingRate = Integer.parseInt(m2.group(1));
                        audio.setSamplingRate(samplingRate);
                        parsed = true;
                      }
                      // Channels.
                      m2 = CHANNELS_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        String ms = m2.group(1);
                        if ("mono".equalsIgnoreCase(ms)) {
                          audio.setChannels(1);
                        } else if ("stereo".equalsIgnoreCase(ms)) {
                          audio.setChannels(2);
                        } else if ("quad".equalsIgnoreCase(ms)) {
                          audio.setChannels(4);
                        }
                        parsed = true;
                      }
                      // Bit rate.
                      m2 = BIT_RATE_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        int bitRate = Integer.parseInt(m2.group(1));
                        audio.setBitRate(bitRate * 1000);
                        parsed = true;
                      }
                      // Bit depth
                      m2 = BIT_DEPTH_PATTERN.matcher(token);
                      if (!parsed && m2.find()) {
                        String bitDepth = m2.group(1);
                        audio.setBitDepth(bitDepth);
                        parsed = true;
                      }
                    }
                  }
                  //reading audio metadata
                  line = reader.readLine();
                  Matcher m4 = p4.matcher(line);
                  if(m4.matches()){
                    line = reader.readLine();
                    while (line != null && p5.matcher(line).matches()) {
                      LOG.debug("Output line: {}", line);
                      Matcher m5 = p5.matcher(line);
                      if (m5.matches()) {
                        audio.getMetadata().put(m5.group(1), m5.group(2));
                      }
                      line = reader.readLine();
                    }
                    reader.reinsertLine(line);
                  } else {
                    reader.reinsertLine(line);
                  }
                  info.setAudio(audio);
                }
              } else // if (m4.matches())
              {
                // Stay on level 2
              }
              /*
                 else
                 {
                 step = 3;
                 }
              */ break;
            }

          default:
            break;
        }
        if (line.startsWith("frame=")) {
          reader.reinsertLine(line);
          break;
        }
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    }
    if (info == null) {
      throw new InputFormatException();
    }
    return info;
  }

  /** @return the readURLOnce */
  public boolean isReadURLOnce() {
    return readURLOnce;
  }

  @Override
  public String toString() {
    if (isURL()) {
      return getURL().toString();
    } else {
      return getFile().getAbsolutePath();
    }
  }
}
