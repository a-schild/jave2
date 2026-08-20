/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 *
 * Copyright (C) 2018- Andre Schild
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.progress.EncoderProgressListener;

/**
 * Two pass encoding (#156).
 *
 * @author a.schild
 */
public class TwoPassEncoderTest extends AMediaTest {

  public TwoPassEncoderTest() {
    super(null, "TwoPassEncoder");
  }

  /** Records what a caller would have been told. */
  private static class RecordingListener implements EncoderProgressListener {
    final List<Integer> permils = new ArrayList<>();
    int sourceInfoCalls = 0;
    int doneCalls = 0;

    @Override
    public void sourceInfo(MultimediaInfo info) {
      sourceInfoCalls++;
    }

    @Override
    public void progress(int permil) {
      permils.add(permil);
    }

    @Override
    public void message(String message) {}

    @Override
    public void done() {
      doneCalls++;
    }
  }

  private EncodingAttributes twoPassAttributes() {
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("aac");
    audio.setBitRate(64000);

    VideoAttributes video = new VideoAttributes();
    video.setCodec("libx264");
    video.setBitRate(200000);

    return new EncodingAttributes()
        .setOutputFormat("mp4")
        .setAudioAttributes(audio)
        .setVideoAttributes(video)
        .setTwoPass(true);
  }

  /** The whole thing: two runs, one playable file, with the audio the second pass put back. */
  @Test
  public void testTwoPassProducesAPlayableFile() throws Exception {
    System.out.println("testTwoPassProducesAPlayableFile");
    File source = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "twoPass.mp4");
    Files.deleteIfExists(target.toPath());

    new Encoder().encode(new MultimediaObject(source), target, twoPassAttributes());

    assertTrue(target.exists(), "No output file");
    assertTrue(target.length() > 0, "Empty output file");

    MultimediaInfo info = new MultimediaObject(target).getInfo();
    assertEquals("mov", info.getFormat());
    assertNotNull(info.getVideo(), "The second pass has to produce video");
    assertNotNull(
        info.getAudio(),
        "Only the first pass drops the audio. The second one was asked for aac and must have it");
  }

  /**
   * The statistics file is this library's business, not the caller's, so nothing may be left
   * lying about once the encoding has finished.
   */
  @Test
  public void testPassLogFilesAreCleanedUp() throws Exception {
    System.out.println("testPassLogFilesAreCleanedUp");
    File source = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "twoPassCleanup.mp4");
    Files.deleteIfExists(target.toPath());

    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    int before = countPassLogs(tempDir);

    new Encoder().encode(new MultimediaObject(source), target, twoPassAttributes());

    assertEquals(before, countPassLogs(tempDir), "Two pass statistics files were left behind");
    assertTrue(
        new File(getResourceTargetPath(), "twoPass.mp4.log").exists() == false,
        "Nothing may be written beside the target either");
  }

  private int countPassLogs(File directory) {
    File[] logs = directory.listFiles((dir, name) -> name.startsWith("jave2-passlog-"));
    return logs == null ? 0 : logs.length;
  }

  /**
   * Two passes have to look like one encoding from the outside: progress that runs up once rather
   * than twice, the source described once, and finished said once at the end.
   */
  @Test
  public void testProgressIsReportedAsASingleEncoding() throws Exception {
    System.out.println("testProgressIsReportedAsASingleEncoding");
    File source = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "twoPassProgress.mp4");
    Files.deleteIfExists(target.toPath());

    RecordingListener listener = new RecordingListener();
    new Encoder()
        .encode(new MultimediaObject(source), target, twoPassAttributes(), listener);

    assertEquals(1, listener.sourceInfoCalls, "The source is one source, however many passes");
    assertEquals(1, listener.doneCalls, "Finished must not be announced after the first pass");

    int previous = -1;
    for (int permil : listener.permils) {
      if (permil == EncoderProgressListener.PROGRESS_UNKNOWN) {
        continue;
      }
      assertTrue(permil >= 0 && permil <= 1000, "Permil out of range: " + permil);
      assertTrue(permil >= previous, "Progress went backwards: " + previous + " then " + permil);
      previous = permil;
    }
  }

  /** Remembers every ffmpeg the encoder started, and what it asked for. */
  private static class RecordingLocator implements ProcessLocator {
    private final ProcessLocator delegate = new DefaultFFMPEGLocator();
    final List<List<String>> invocations = new ArrayList<>();

    @Override
    public String getExecutablePath() {
      return delegate.getExecutablePath();
    }

    @Override
    public ProcessWrapper createExecutor() {
      final List<String> arguments = new ArrayList<>();
      invocations.add(arguments);
      return new ProcessWrapper(delegate.getExecutablePath()) {
        @Override
        public void addArgument(String arg) {
          arguments.add(arg);
          super.addArgument(arg);
        }
      };
    }
  }

  /**
   * Two passes really means two runs of ffmpeg, numbered, sharing one statistics file, with the
   * first told to keep nothing. Asserted on the command lines, because everything else about a two
   * pass encoding looks identical to a single pass from the outside, so a flag that was quietly
   * ignored would pass every other test here.
   */
  @Test
  public void testItRunsFfmpegTwiceWithTheRightArguments() throws Exception {
    System.out.println("testItRunsFfmpegTwiceWithTheRightArguments");
    File source = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "twoPassArguments.mp4");
    Files.deleteIfExists(target.toPath());

    RecordingLocator locator = new RecordingLocator();
    new Encoder(locator).encode(new MultimediaObject(source), target, twoPassAttributes());

    assertEquals(2, locator.invocations.size(), "Expected two ffmpeg runs");

    List<String> first = locator.invocations.get(0);
    List<String> second = locator.invocations.get(1);

    assertEquals("1", valueOf(first, "-pass"), "First run is not pass 1");
    assertEquals("2", valueOf(second, "-pass"), "Second run is not pass 2");

    assertEquals(
        valueOf(first, "-passlogfile"),
        valueOf(second, "-passlogfile"),
        "The passes must share one statistics file, or the second learns nothing");

    // The first pass only measures, so it keeps neither the audio nor the pictures.
    assertTrue(first.contains("-an"), "The first pass should not bother with the audio");
    assertEquals("null", valueOf(first, "-f"), "The first pass should discard its output");
    assertEquals("-", first.get(first.size() - 1), "The first pass should not write a file");

    assertEquals(
        target.getAbsolutePath(),
        second.get(second.size() - 1),
        "The second pass writes the real target");
    assertEquals("mp4", valueOf(second, "-f"), "The second pass keeps the wanted format");
  }

  /** The value following a flag, taking the last one when it appears more than once. */
  private String valueOf(List<String> arguments, String flag) {
    String value = null;
    for (int i = 0; i < arguments.size() - 1; i++) {
      if (flag.equals(arguments.get(i))) {
        value = arguments.get(i + 1);
      }
    }
    return value;
  }

  /**
   * Two pass exists to spend a video bitrate well, so asking for it without one is a mistake worth
   * reporting rather than quietly running ffmpeg twice for nothing.
   */
  @Test
  public void testTwoPassNeedsAVideoBitrate() {
    System.out.println("testTwoPassNeedsAVideoBitrate");

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("aac");

    EncodingAttributes audioOnly =
        new EncodingAttributes()
            .setOutputFormat("mp4")
            .setAudioAttributes(audio)
            .setTwoPass(true);

    IllegalArgumentException noVideo =
        assertThrows(IllegalArgumentException.class, audioOnly::validate);
    assertTrue(
        noVideo.getMessage().contains("video attributes"),
        "Unhelpful message: " + noVideo.getMessage());

    VideoAttributes noBitrate = new VideoAttributes();
    noBitrate.setCodec("libx264");
    noBitrate.setCrf(23);

    EncodingAttributes crfInstead =
        new EncodingAttributes()
            .setOutputFormat("mp4")
            .setVideoAttributes(noBitrate)
            .setTwoPass(true);

    IllegalArgumentException noBudget =
        assertThrows(IllegalArgumentException.class, crfInstead::validate);
    assertTrue(
        noBudget.getMessage().contains("video bitrate"),
        "Unhelpful message: " + noBudget.getMessage());
  }

  /** Leaving two pass off has to go on behaving exactly as before. */
  @Test
  public void testSinglePassIsUnaffected() throws Exception {
    System.out.println("testSinglePassIsUnaffected");
    File source = new File(getResourceSourcePath(), "small.mp4");
    File target = new File(getResourceTargetPath(), "singlePass.mp4");
    Files.deleteIfExists(target.toPath());

    EncodingAttributes attributes = twoPassAttributes().setTwoPass(false);

    RecordingListener listener = new RecordingListener();
    new Encoder().encode(new MultimediaObject(source), target, attributes, listener);

    assertTrue(target.exists(), "No output file");
    assertEquals(1, listener.doneCalls);
    assertEquals(1, listener.sourceInfoCalls);
  }
}
