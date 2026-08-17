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
package ws.schild.jave.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ffmpeg process wrapper.
 *
 * @author Carlo Pelliccia
 */
public class ProcessWrapper implements AutoCloseable {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessWrapper.class);

  /**
   * Which executables take {@code -fps_mode} instead of {@code -vsync}, keyed by their path.
   * Finding out costs a process start, and the answer cannot change while that file is in place,
   * so it is asked once.
   */
  private static final Map<String, Boolean> FPS_MODE_SUPPORT = new ConcurrentHashMap<>();

  /** The path of the ffmpeg executable. */
  private final String ffmpegExecutablePath;

  /** Arguments for the executable. */
  private final ArrayList<String> args = new ArrayList<>();

  /**
   * The working directory of the ffmpeg process. When null the process inherits the working
   * directory of the jvm, which is the previous and still default behaviour. Setting it matters for
   * arguments ffmpeg resolves itself, such as the file list of the concat demuxer or a font file
   * referenced from a filter.
   */
  private File execFolder = null;

  /** The process representing the ffmpeg execution. */
  private Process ffmpeg = null;

  /**
   * A process killer to kill the ffmpeg process with a shutdown hook, useful if the jvm execution
   * is shutted down during an ongoing encoding process.
   */
  private ProcessKiller ffmpegKiller = null;

  /** A stream reading from the ffmpeg process standard output channel. */
  private InputStream inputStream = null;

  /** A stream writing in the ffmpeg process standard input channel. */
  private OutputStream outputStream = null;

  /** A stream reading from the ffmpeg process standard error channel. */
  private InputStream errorStream = null;

  /**
   * It build the executor.
   *
   * @param ffmpegExecutablePath The path of the ffmpeg executable.
   */
  public ProcessWrapper(String ffmpegExecutablePath) {
    this.ffmpegExecutablePath = ffmpegExecutablePath;
  }

  /**
   * Adds an argument to the ffmpeg executable call.
   *
   * @param arg The argument.
   */
  public void addArgument(String arg) {
    args.add(arg);
  }

  /**
   * Executes the ffmpeg process with the previous given arguments.
   *
   * @param destroyOnRuntimeShutdown destroy process if the runtime VM is shutdown
   * @param openIOStreams Open IO streams for input/output and errorout, should be false when
   *     destroyOnRuntimeShutdown is false too
   * @throws IOException If the process call fails.
   */
  public void execute(boolean destroyOnRuntimeShutdown, boolean openIOStreams) throws IOException {
    Stream<String> execArgs =
        Stream.concat(Stream.of(ffmpegExecutablePath), adaptToExecutable(args).stream());

    execArgs = enhanceArguments(execArgs);

    List<String> execList = execArgs.collect(Collectors.toList());

    if (LOG.isDebugEnabled()) {
      LOG.debug("About to execute {}", execList.stream().collect(Collectors.joining(" ")));
    }

    Runtime runtime = Runtime.getRuntime();
    // A null envp inherits the environment of the jvm, a null execFolder its working directory
    ffmpeg = runtime.exec(execList.toArray(new String[0]), null, execFolder);

    if (destroyOnRuntimeShutdown) {
      ffmpegKiller = new ProcessKiller(ffmpeg);
      runtime.addShutdownHook(ffmpegKiller);
    }

    if (openIOStreams) {
      inputStream = ffmpeg.getInputStream();
      outputStream = ffmpeg.getOutputStream();
      errorStream = ffmpeg.getErrorStream();
    }
  }

  /**
   * Provide an opportunity for subclasses to enhance the argument list before passing off to
   * execute.
   *
   * @param execArgs The current Stream of arguments
   * @return A possibly enhanced stream of arguments
   */
  protected Stream<String> enhanceArguments(Stream<String> execArgs) {
    return execArgs;
  }

  /**
   * Renames the arguments this particular ffmpeg would not understand.
   *
   * <p>So far that is only {@code -vsync}, which ffmpeg has removed in favour of {@code -fps_mode}.
   * The two never overlapped, the old releases this library still bundles for some platforms know
   * only {@code -vsync} and the current ones know only {@code -fps_mode}, so which to send cannot
   * be decided when the library is built, only when the executable in front of us is known.
   *
   * <p>Note that {@code drop} has no counterpart under {@code -fps_mode}. It is passed through
   * unchanged so that ffmpeg says so itself rather than the library silently substituting
   * something else.
   *
   * @param arguments The arguments as the caller built them.
   * @return The arguments this executable accepts, the same list when nothing needed renaming.
   */
  private List<String> adaptToExecutable(List<String> arguments) {
    if (!arguments.contains("-vsync") || !supportsFpsMode()) {
      return arguments;
    }
    LOG.debug("This ffmpeg wants -fps_mode rather than -vsync, renaming the argument");
    return arguments.stream()
        .map(argument -> "-vsync".equals(argument) ? "-fps_mode" : argument)
        .collect(Collectors.toList());
  }

  /** Whether this executable takes {@code -fps_mode}, probed once per executable and remembered. */
  private boolean supportsFpsMode() {
    return FPS_MODE_SUPPORT.computeIfAbsent(ffmpegExecutablePath, ProcessWrapper::probeFpsMode);
  }

  /**
   * Asks the executable itself, by handing it the option and seeing whether it objects. Cheaper and
   * far more dependable than reading a version out of the banner, which the various builds format
   * as they please.
   *
   * @param executable The ffmpeg to ask.
   * @return Whether it accepts {@code -fps_mode}.
   */
  private static boolean probeFpsMode(String executable) {
    Process probe = null;
    try {
      probe =
          Runtime.getRuntime()
              .exec(new String[] {executable, "-hide_banner", "-fps_mode", "cfr"});
      StringBuilder complaints = new StringBuilder();
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(probe.getErrorStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          complaints.append(line).append('\n');
        }
      }
      probe.waitFor();
      boolean supported = !complaints.toString().contains("Unrecognized option 'fps_mode'");
      LOG.debug("<{}> accepts -fps_mode: {}", executable, supported);
      return supported;
    } catch (IOException e) {
      LOG.warn("Could not ask <{}> about -fps_mode, assuming it wants -vsync", executable, e);
      return false;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOG.warn("Interrupted while asking <{}> about -fps_mode", executable, e);
      return false;
    } finally {
      if (probe != null) {
        probe.destroy();
      }
    }
  }

  /**
   * Returns the working directory the ffmpeg process will be started in.
   *
   * @return The working directory, null when the jvm working directory is used.
   */
  public File getExecFolder() {
    return execFolder;
  }

  /**
   * Sets the working directory the ffmpeg process will be started in. Has to be called before
   * {@link #execute()}.
   *
   * @param execFolder The working directory, null to use the jvm working directory.
   */
  public void setExecFolder(File execFolder) {
    this.execFolder = execFolder;
  }

  /**
   * Executes the ffmpeg process with the previous given arguments. Default to kill processes when
   * the JVM terminates, and the various IOStreams are opened as required
   *
   * @throws IOException If the process call fails.
   */
  public void execute() throws IOException {
    execute(true, true);
  }

  /**
   * Returns a stream reading from the ffmpeg process standard output channel.
   *
   * @return A stream reading from the ffmpeg process standard output channel.
   */
  public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * Returns a stream writing in the ffmpeg process standard input channel.
   *
   * @return A stream writing in the ffmpeg process standard input channel.
   */
  public OutputStream getOutputStream() {
    return outputStream;
  }

  /**
   * Returns a stream reading from the ffmpeg process standard error channel.
   *
   * @return A stream reading from the ffmpeg process standard error channel.
   */
  public InputStream getErrorStream() {
    return errorStream;
  }

  /** If there's a ffmpeg execution in progress, it kills it. */
  public void destroy() {
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (Throwable t) {
        LOG.warn("Error closing input stream", t);
      }
      inputStream = null;
    }

    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (Throwable t) {
        LOG.warn("Error closing output stream", t);
      }
      outputStream = null;
    }

    if (errorStream != null) {
      try {
        errorStream.close();
      } catch (Throwable t) {
        LOG.warn("Error closing error stream", t);
      }
      errorStream = null;
    }

    if (ffmpeg != null) {
      ffmpeg.destroy();
      ffmpeg = null;
    }

    if (ffmpegKiller != null) {
      Runtime runtime = Runtime.getRuntime();
      runtime.removeShutdownHook(ffmpegKiller);
      ffmpegKiller = null;
    }
  }

  /**
   * Return the exit code of the ffmpeg process If the process is not yet terminated, it waits for
   * the termination of the process
   *
   * @return process exit code
   */
  public int getProcessExitCode() {
    // Make sure it's terminated
    try {
      ffmpeg.waitFor();
    } catch (InterruptedException ex) {
      LOG.warn("Interrupted during waiting on process, forced shutdown?", ex);
    }
    return ffmpeg.exitValue();
  }

  @Override
  public void close() {
    destroy();
  }
}
