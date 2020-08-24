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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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

  /** The path of the ffmpeg executable. */
  private final String ffmpegExecutablePath;

  /** Arguments for the executable. */
  private final ArrayList<String> args = new ArrayList<>();

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
    Stream<String> execArgs = Stream.concat(Stream.of(ffmpegExecutablePath), args.stream());

    execArgs = enhanceArguments(execArgs);

    List<String> execList = execArgs.collect(Collectors.toList());

    if (LOG.isDebugEnabled()) {
      LOG.debug("About to execute {}", execList.stream().collect(Collectors.joining(" ")));
    }

    Runtime runtime = Runtime.getRuntime();
    ffmpeg = runtime.exec(execList.toArray(new String[0]));

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
