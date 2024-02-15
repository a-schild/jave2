/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 *
 * Copyright (C) 2008-2009 Carlo Pelliccia (www.sauronsoftware.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ws.schild.jave;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.schild.jave.encode.ArgType;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingArgument;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.PredicateArgument;
import ws.schild.jave.encode.ValueArgument;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.encode.VideoFilterArgument;
import ws.schild.jave.encode.enums.TuneEnum;
import ws.schild.jave.encode.enums.VsyncMethod;
import ws.schild.jave.encode.enums.X264_PROFILE;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.progress.EncoderProgressListener;
import ws.schild.jave.utils.RBufferedReader;

/**
 * Main class of the package. Instances can encode audio and video streams.
 *
 * @author Carlo Pelliccia
 */
public class Encoder {

  private static final Logger LOG = LoggerFactory.getLogger(Encoder.class);

  /** This regexp is used to parse the ffmpeg output about the supported formats. */
  private static final Pattern FORMAT_PATTERN =
      Pattern.compile("^\\s*([D ])([E ])\\s+([\\w,]+)\\s+.+$");

  /** This regexp is used to parse the ffmpeg output about the included encoders/decoders. */
  private static final Pattern ENCODER_DECODER_PATTERN =
      Pattern.compile("^\\s*([AVS]).{5}\\s(\\S+).(.+)$", Pattern.CASE_INSENSITIVE);

  /** This regexp is used to parse the ffmpeg output about the success of an encoding operation. */
  private static final Pattern SUCCESS_PATTERN =
      Pattern.compile(
          "^\\s*video\\:\\S+\\s+audio\\:\\S+\\s+subtitle\\:\\S+\\s+global headers\\:\\S+.*$",
          Pattern.CASE_INSENSITIVE);

  /** The locator of the ffmpeg executable used by this encoder. */
  private final ProcessLocator locator;

  /**
   * The executor used to do the conversion Is saved here, so we can abort the conversion process
   */
  private ProcessWrapper ffmpeg;

  /** List of unhandled messages from ffmpeng run */
  private List<String> unhandledMessages = null;

  /**
   * It builds an encoder using a {@link DefaultFFMPEGLocator} instance to locate the ffmpeg
   * executable to use.
   */
  public Encoder() {
    this.locator = new DefaultFFMPEGLocator();
  }

  /**
   * It builds an encoder with a custom {@link ws.schild.jave.process.ffmpeg.FFMPEGProcess}.
   *
   * @param locator The locator picking up the ffmpeg executable used by the encoder.
   */
  public Encoder(ProcessLocator locator) {
    this.locator = locator;
  }

  /**
   * Returns a list with the names of all the audio decoders bundled with the ffmpeg distribution in
   * use. An audio stream can be decoded only if a decoder for its format is available.
   *
   * @return A list with the names of all the included audio decoders.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public String[] getAudioDecoders() throws EncoderException {
    return getCoders(false, true);
  }

  /**
   * Returns a list with the names of all the audio encoders bundled with the ffmpeg distribution in
   * use. An audio stream can be encoded using one of these encoders.
   *
   * @return A list with the names of all the included audio encoders.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public String[] getAudioEncoders() throws EncoderException {
    return getCoders(true, true);
  }

  /**
   * Returns a list with the names of all the coders bundled with the ffmpeg distribution in use.
   *
   * @param encoder Do search encoders, else decoders
   * @param audio Do search for audio encodes, else video
   * @return A list with the names of all the included encoders
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  protected String[] getCoders(boolean encoder, boolean audio) throws EncoderException {
    ArrayList<String> res = new ArrayList<>();
    ProcessWrapper localFFMPEG = locator.createExecutor();
    localFFMPEG.addArgument(encoder ? "-encoders" : "-decoders");
    try {
      localFFMPEG.execute();
      RBufferedReader reader =
          new RBufferedReader(new InputStreamReader(localFFMPEG.getInputStream()));
      String line;
      String format = audio ? "A" : "V";
      boolean headerFound = false;
      boolean evaluateLine = false;
      while ((line = reader.readLine()) != null) {
        if (line.trim().length() == 0) {
          continue;
        }
        if (headerFound) {
          if (evaluateLine) {
            Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);
            if (matcher.matches()) {
              // String encoderFlag = matcher.group(2);
              String audioVideoFlag = matcher.group(1);
              if (format.equals(audioVideoFlag)) {
                String name = matcher.group(2);
                res.add(name);
              }
            } else {
              break;
            }
          } else {
            evaluateLine = line.trim().equals("------");
          }
        } else if (line.trim().equals(encoder ? "Encoders:" : "Decoders:")) {
          headerFound = true;
        }
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    } finally {
      localFFMPEG.destroy();
    }
    int size = res.size();
    String[] ret = new String[size];
    for (int i = 0; i < size; i++) {
      ret[i] = res.get(i);
    }
    return ret;
  }

  /**
   * Returns a list with the names of all the video decoders bundled with the ffmpeg distribution in
   * use. A video stream can be decoded only if a decoder for its format is available.
   *
   * @return A list with the names of all the included video decoders.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public String[] getVideoDecoders() throws EncoderException {
    return getCoders(false, false);
  }

  /**
   * Returns a list with the names of all the video encoders bundled with the ffmpeg distribution in
   * use. A video stream can be encoded using one of these encoders.
   *
   * @return A list with the names of all the included video encoders.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public String[] getVideoEncoders() throws EncoderException {
    return getCoders(true, false);
  }

  /**
   * Returns a list with the names of all the file formats supported at encoding time by the
   * underlying ffmpeg distribution. A multimedia file could be encoded and generated only if the
   * specified format is in this list.
   *
   * @return A list with the names of all the supported file formats at encoding time.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public String[] getSupportedEncodingFormats() throws EncoderException {
    return getSupportedCodingFormats(true);
  }

  /**
   * Returns a list with the names of all the file formats supported at en/de-coding time by the
   * underlying ffmpeg distribution.A multimedia file could be encoded and generated only if the
   * specified format is in this list.
   *
   * @param encoding True for encoding job, false to decode a file
   * @return A list with the names of all the supported file formats at encoding time.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  /*
   * TODO: Refactor this out to a parsing utility. This will enable us to support multiple ffmpeg
   * versions if the structure changes.
   */
  protected String[] getSupportedCodingFormats(boolean encoding) throws EncoderException {
    ArrayList<String> res = new ArrayList<>();

    try (ProcessWrapper localFFMPEG = locator.createExecutor()) {
      localFFMPEG.addArgument("-formats");

      localFFMPEG.execute();
      RBufferedReader reader =
          new RBufferedReader(new InputStreamReader(localFFMPEG.getInputStream()));
      String line;
      String ed = encoding ? "E" : "D";
      boolean headerFound = false;
      boolean evaluateLine = false;
      while ((line = reader.readLine()) != null) {
        if (line.trim().length() == 0) {
          continue;
        }
        if (headerFound) {
          if (evaluateLine) {
            Matcher matcher = FORMAT_PATTERN.matcher(line);
            if (matcher.matches()) {
              String encoderFlag = matcher.group(encoding ? 2 : 1);
              if (ed.equals(encoderFlag)) {
                String aux = matcher.group(3);
                StringTokenizer st = new StringTokenizer(aux, ",");
                while (st.hasMoreTokens()) {
                  String token = st.nextToken().trim();
                  if (!res.contains(token)) {
                    res.add(token);
                  }
                }
              }
            } else {
              break;
            }
          } else {
            evaluateLine = line.trim().equals("--");
          }
        } else if (line.trim().equals("File formats:")) {
          headerFound = true;
        }
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    }

    int size = res.size();
    String[] ret = new String[size];
    for (int i = 0; i < size; i++) {
      ret[i] = res.get(i);
    }
    return ret;
  }

  /**
   * Returns a list with the names of all the file formats supported at decoding time by the
   * underlying ffmpeg distribution. A multimedia file could be open and decoded only if its format
   * is in this list.
   *
   * @return A list with the names of all the supported file formats at decoding time.
   * @throws EncoderException If a problem occurs calling the underlying ffmpeg executable.
   */
  public String[] getSupportedDecodingFormats() throws EncoderException {
    return getSupportedCodingFormats(false);
  }

  /**
   * Re-encode a multimedia file(s).
   *
   * <p>This method is not reentrant, instead create multiple object instances
   *
   * @param multimediaObject The source multimedia file. It cannot be null. Be sure this file can be
   *     decoded (see null null null null {@link Encoder#getSupportedDecodingFormats()}, {@link
   *     Encoder#getAudioDecoders()} and {@link Encoder#getVideoDecoders()}). When passing multiple
   *     sources, make sure that they are compatible in the way that ffmpeg can concat them. We
   *     don't use the complex filter at the moment Perhaps you will need to first transcode/resize
   *     them https://trac.ffmpeg.org/wiki/Concatenate @see "Concat protocol"
   * @param target The target multimedia re-encoded file. It cannot be null. If this file already
   *     exists, it will be overwrited.
   * @param attributes A set of attributes for the encoding process.
   * @throws IllegalArgumentException If both audio and video parameters are null.
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void encode(MultimediaObject multimediaObject, File target, EncodingAttributes attributes)
      throws IllegalArgumentException, InputFormatException, EncoderException {
    encode(multimediaObject, target, attributes, null);
  }

  public void encode(
      List<MultimediaObject> multimediaObjects, File target, EncodingAttributes attributes)
      throws IllegalArgumentException, InputFormatException, EncoderException {
    encode(multimediaObjects, target, attributes, null);
  }

  /**
   * Re-encode a multimedia file.
   *
   * <p>This method is not reentrant, instead create multiple object instances
   *
   * @param multimediaObject The source multimedia file. It cannot be null. Be sure this file can be
   *     decoded (see null null null null {@link Encoder#getSupportedDecodingFormats()}, {@link
   *     Encoder#getAudioDecoders()} and {@link Encoder#getVideoDecoders()}).
   * @param target The target multimedia re-encoded file. It cannot be null. If this file already
   *     exists, it will be overwrited.
   * @param attributes A set of attributes for the encoding process.
   * @param listener An optional progress listener for the encoding process. It can be null.
   * @throws IllegalArgumentException If both audio and video parameters are null.
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void encode(
      MultimediaObject multimediaObject,
      File target,
      EncodingAttributes attributes,
      EncoderProgressListener listener)
      throws IllegalArgumentException, InputFormatException, EncoderException {
    List<MultimediaObject> src = new ArrayList<>();
    src.add(multimediaObject);
    encode(src, target, attributes, listener);
  }

  private static List<EncodingArgument> globalOptions =
      new ArrayList<>(Arrays.asList(
          new ValueArgument(ArgType.GLOBAL, "--filter_thread",
              ea -> ea.getFilterThreads().map(Object::toString)),
          new ValueArgument(ArgType.GLOBAL, "-ss", ea -> ea.getOffset().map(Object::toString)),
          new ValueArgument(ArgType.INFILE, "-threads", 
        	  ea -> ea.getDecodingThreads().map(Object::toString)),
          new PredicateArgument(ArgType.INFILE, "-loop", "1", 
        	  ea -> ea.getLoop() && ea.getDuration().isPresent()),
          new ValueArgument(ArgType.INFILE, "-f", ea -> ea.getInputFormat()),
          new ValueArgument(ArgType.INFILE, "-safe", ea -> ea.getSafe().map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-t", ea -> ea.getDuration().map(Object::toString)),
          // Video Options
          new PredicateArgument(ArgType.OUTFILE, "-vn", ea -> !ea.getVideoAttributes().isPresent()),
          new ValueArgument(ArgType.OUTFILE, "-vcodec",
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getCodec)),
          new ValueArgument(ArgType.OUTFILE, "-vtag",
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getTag)),
          new ValueArgument(ArgType.OUTFILE, "-vb",
              ea -> ea.getVideoAttributes()
                      .flatMap(VideoAttributes::getBitRate)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-r",
              ea -> ea.getVideoAttributes()
                      .flatMap(VideoAttributes::getFrameRate)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-s",
              ea -> ea.getVideoAttributes()
                      .flatMap(VideoAttributes::getSize)
                      .map(VideoSize::asEncoderArgument)),
          new PredicateArgument(ArgType.OUTFILE, "-movflags", "faststart",
              ea -> ea.getVideoAttributes().isPresent()),
          new ValueArgument(ArgType.OUTFILE, "-profile:v",
              ea -> ea.getVideoAttributes()
                      .flatMap(VideoAttributes::getX264Profile)
                      .map(X264_PROFILE::getModeName)),
          new VideoFilterArgument(ArgType.OUTFILE,
              ea -> ea.getVideoAttributes()
                      .map(VideoAttributes::getVideoFilters)
                      .map(Collection::stream)
                      .map(s -> s.flatMap(vf -> Stream.of(vf.getExpression())))
                      .orElseGet(Stream::empty)),
          new ValueArgument(ArgType.OUTFILE, "-filter_complex",
              ea -> ea.getVideoAttributes()
                      .flatMap(VideoAttributes::getComplexFiltergraph)
                      .map(FilterGraph::getExpression)),
          new ValueArgument(ArgType.OUTFILE, "-qscale:v",
              ea -> ea.getVideoAttributes()
                      .flatMap(VideoAttributes::getQuality)
                      .map(Object::toString)),
          // Audio Options
          new PredicateArgument(ArgType.OUTFILE, "-an", ea -> !ea.getAudioAttributes().isPresent()),
          new ValueArgument(ArgType.OUTFILE, "-acodec",
              ea -> ea.getAudioAttributes().flatMap(AudioAttributes::getCodec)),
          new ValueArgument(ArgType.OUTFILE, "-ab",
              ea -> ea.getAudioAttributes()
                      .flatMap(AudioAttributes::getBitRate)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-ac",
              ea -> ea.getAudioAttributes()
                      .flatMap(AudioAttributes::getChannels)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-ar",
              ea -> ea.getAudioAttributes()
                      .flatMap(AudioAttributes::getSamplingRate)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-vol",
              ea -> ea.getAudioAttributes()
                      .flatMap(AudioAttributes::getVolume)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-qscale:a",
              ea -> ea.getAudioAttributes()
                      .flatMap(AudioAttributes::getQuality)
                      .map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-f", ea -> ea.getOutputFormat()),
          new ValueArgument(ArgType.OUTFILE, "-threads", 
              ea -> ea.getEncodingThreads().map(Object::toString)),
          new PredicateArgument(ArgType.OUTFILE, "-map_metadata", "0", 
              ea -> ea.isMapMetaData()),
          new ValueArgument(ArgType.OUTFILE, "-pix_fmt", 
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getPixelFormat)),
          new ValueArgument(ArgType.OUTFILE, "-vsync",
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getVsync).map(VsyncMethod::getMethodName)),
          new ValueArgument(ArgType.OUTFILE, "-crf",
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getCrf).map(Object::toString)),
          new ValueArgument(ArgType.OUTFILE, "-preset",
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getPreset)),
	  new ValueArgument(ArgType.OUTFILE, "-tune",
              ea -> ea.getVideoAttributes().flatMap(VideoAttributes::getTune).map(TuneEnum::getTuneName))    
        )
      );

  public static void addOptionAtIndex(EncodingArgument arg, Integer index) {
    globalOptions.add(index, arg);
  }

  public static void removeOptionAtIndex(Integer index) {
    globalOptions.remove(index);
  }

  public static void setOptionAtIndex(EncodingArgument arg, Integer index) {
    globalOptions.set(index, arg);
  }

  public static EncodingArgument setOptionAtIndex(Integer index) {
    return globalOptions.get(index);
  }

  /**
   * Re-encode a multimedia file(s).
   *
   * <p>This method is not reentrant, instead create multiple object instances
   *
   * @param multimediaObjects The source multimedia files. It cannot be null. Be sure this file can
   *     be decoded (see null null null null {@link Encoder#getSupportedDecodingFormats()}, {@link
   *     Encoder#getAudioDecoders()} and* {@link Encoder#getVideoDecoders()}) When passing multiple
   *     sources, make sure that they are compatible in the way that ffmpeg can concat them. We
   *     don't use the complex filter at the moment Perhaps you will need to first transcode/resize
   *     them https://trac.ffmpeg.org/wiki/Concatenate @see "Concat protocol"
   * @param target The target multimedia re-encoded file. It cannot be null. If this file already
   *     exists, it will be overwrited.
   * @param attributes A set of attributes for the encoding process.
   * @param listener An optional progress listener for the encoding process. It can be null.
   * @throws IllegalArgumentException If both audio and video parameters are null.
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */
  public void encode(
      List<MultimediaObject> multimediaObjects,
      File target,
      EncodingAttributes attributes,
      EncoderProgressListener listener)
      throws IllegalArgumentException, InputFormatException, EncoderException {
    encode(multimediaObjects,target,attributes,listener,null);
  }

  /**
   * Re-encode a multimedia file(s).
   *
   * <p>This method is not reentrant, instead create multiple object instances
   *
   * @param multimediaObjects The source multimedia files. It cannot be null. Be sure this file can
   *     be decoded (see null null null null {@link Encoder#getSupportedDecodingFormats()}, {@link
   *     Encoder#getAudioDecoders()} and* {@link Encoder#getVideoDecoders()}) When passing multiple
   *     sources, make sure that they are compatible in the way that ffmpeg can concat them. We
   *     don't use the complex filter at the moment Perhaps you will need to first transcode/resize
   *     them https://trac.ffmpeg.org/wiki/Concatenate @see "Concat protocol"
   * @param target The target multimedia re-encoded file. It cannot be null. If this file already
   *     exists, it will be overwrited.
   * @param attributes A set of attributes for the encoding process.
   * @param listener An optional progress listener for the encoding process. It can be null.
   * @param currOptions Set more global options It can be null.
   * @throws IllegalArgumentException If both audio and video parameters are null.
   * @throws InputFormatException If the source multimedia file cannot be decoded.
   * @throws EncoderException If a problems occurs during the encoding process.
   */


  public void encode(
          List<MultimediaObject> multimediaObjects,
          File target,
          EncodingAttributes attributes,
          EncoderProgressListener listener,
          List<EncodingArgument> currOptions)
          throws IllegalArgumentException, InputFormatException, EncoderException {
    attributes.validate();

    target = target.getAbsoluteFile();
    target.getParentFile().mkdirs();
    ffmpeg = locator.createExecutor();

    // Set global options
    globalOptions
        .stream()
        .filter(ea -> ArgType.GLOBAL.equals(ea.getArgType()))
        .flatMap(eArg -> eArg.getArguments(attributes))
        .forEach(ffmpeg::addArgument);
    if (currOptions != null) {
        currOptions
          .stream()
          .filter(ea -> ArgType.GLOBAL.equals(ea.getArgType()))
          .flatMap(eArg -> eArg.getArguments(attributes))
          .forEach(ffmpeg::addArgument);
    }

    // Set input options, must be before -i argument
    globalOptions
        .stream()
        .filter(ea -> ArgType.INFILE.equals(ea.getArgType()))
        .flatMap(eArg -> eArg.getArguments(attributes))
        .forEach(ffmpeg::addArgument);
    if (currOptions != null) {
      currOptions
          .stream()
          .filter(ea -> ArgType.INFILE.equals(ea.getArgType()))
          .flatMap(eArg -> eArg.getArguments(attributes))
          .forEach(ffmpeg::addArgument);
    }

    multimediaObjects
        .stream()
        .map(Object::toString)
        .flatMap(mmo -> Stream.of("-i", mmo))
        .forEach(ffmpeg::addArgument);

    // Set output options. Must be after the -i and before the outfile target
    globalOptions
        .stream()
        .filter(ea -> ArgType.OUTFILE.equals(ea.getArgType()))
        .flatMap(eArg -> eArg.getArguments(attributes))
        .forEach(ffmpeg::addArgument);

    if (currOptions != null) {
      currOptions
          .stream()
          .filter(ea -> ArgType.OUTFILE.equals(ea.getArgType()))
          .flatMap(eArg -> eArg.getArguments(attributes))
          .forEach(ffmpeg::addArgument);
    }

    ffmpeg.addArgument("-y");
    ffmpeg.addArgument(target.getAbsolutePath());

    try {
      ffmpeg.execute();
    } catch (IOException e) {
      throw new EncoderException(e);
    }

    try {
      String lastWarning = null;
      long duration = 0;
      MultimediaInfo info = null;
      /*
       * TODO: This is an awkward way of determining duration of input videos. This calls a separate
       * FFMPEG process to getInfo when the output of running FFMPEG just above will list the info
       * of the input videos as "Input #0" -> "Input #N". Capture _that_ output instead of calling
       * *back* into FFMPEG. Furthermore, expressing the percentage of the transcoding job as a
       * simple "what percentage of the input duration have we output" feels too naive given all of
       * the interesting video filters that can be applied. It feels like the user would know the
       * duration of the output video as:
       * 1. The duration of the input video (as we have expressed here)
       * 2. The sum of the durations of the input videos
       * 3. A particular duration calculated with the context of all the inputs/encoding attributes.
       * So, if the calling method tells this method the expected duration, then we can express 
       * progress as a percentage. I would like to make #1 and #2 very simple to do, however.
       * Perhaps a method that would take the input MultimediaInfo objects that are generated from
       * this FFMPEG invocation, the EncodingAttributes, and would output a duration. Then we could
       * have named methods that would calculate durations as in #1 and #2. 
       */
      if (multimediaObjects.size() == 1
          && !multimediaObjects.get(0).isReadURLOnce()) {
        info = multimediaObjects.get(0).getInfo();
      }

      Float offsetAttribute = attributes.getOffset().orElse(null);
      Float durationAttribute = attributes.getDuration().orElse(null);
      if (durationAttribute != null) {
        duration = (long) Math.round((durationAttribute * 1000L));
      } else {
        if (info != null) {
          duration = info.getDuration();
          if (offsetAttribute != null) {
            duration -= (long) Math.round((offsetAttribute * 1000L));
          }
        }
      }

      if (listener != null) {
        listener.sourceInfo(info);
      }
      String line;
      ConversionOutputAnalyzer outputAnalyzer = new ConversionOutputAnalyzer(duration, listener);
      RBufferedReader reader = new RBufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
      while ((line = reader.readLine()) != null) {
        outputAnalyzer.analyzeNewLine(line);
      }
      if (outputAnalyzer.getLastWarning() != null) {
        if (!SUCCESS_PATTERN.matcher(lastWarning).matches()) {
          throw new EncoderException("No match for: " + SUCCESS_PATTERN + " in " + lastWarning);
        }
      }
      /*
       * TODO: This is not thread safe. This needs to be a resulting value from the call to the 
       * Encoder. We can create a separate EncoderResult, but not a stateful variable.
       */
      unhandledMessages = outputAnalyzer.getUnhandledMessages();
      int exitCode = ffmpeg.getProcessExitCode();
      if (exitCode != 0) {
        LOG.error("Process exit code: {}  to {}", exitCode, target.getName());
        throw new EncoderException("Exit code of ffmpeg encoding run is " + exitCode);
      }
    } catch (IOException e) {
      throw new EncoderException(e);
    } finally {
      if (ffmpeg != null) {
        ffmpeg.destroy();
      }
      ffmpeg = null;
    }
  }

  /**
   * Return the list of unhandled output messages of the ffmpeng encoder run
   *
   * @return the unhandledMessages list of unhandled messages, can be null or empty
   */
  public List<String> getUnhandledMessages() {
    return unhandledMessages;
  }

  /** Force the encoding process to stop */
  public void abortEncoding() {
    if (ffmpeg != null) {
      ffmpeg.destroy();
      ffmpeg = null;
    }
  }
  
  public void quitEncoding() throws IOException { // https://stackoverflow.com/a/21032143/12857692
	  final OutputStream ingreso = this.ffmpeg.getOutputStream();
	  final byte b[] = "q".getBytes(StandardCharsets.US_ASCII);
	  ingreso.write(b);
	  ingreso.flush();
  }
}
