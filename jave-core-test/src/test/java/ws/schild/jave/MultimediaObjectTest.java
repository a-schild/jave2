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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ProcessWrapper;

/** @author a.schild */
public class MultimediaObjectTest extends AMediaTest {

  public MultimediaObjectTest() {
    super(null, "MultimediaObject");
  }

  /**
   * A generous timeout must not get in the way of a local file, which is read in milliseconds.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfoWithTimeoutSucceeds() throws Exception {
    System.out.println("testGetInfoWithTimeoutSucceeds");
    File file = new File(getResourceSourcePath(), "dance1.avi");
    MultimediaObject instance = new MultimediaObject(file);

    MultimediaInfo result = instance.getInfo(30000L);

    assertNotNull(result, "No info returned within the timeout");
    assertEquals("avi", result.getFormat(), "Invalid video format");
  }

  /**
   * A timeout too short for ffmpeg to finish has to end the call, rather than let it run on, and
   * has to say so rather than report the source as unreadable.
   */
  @Test
  public void testGetInfoTimesOut() {
    System.out.println("testGetInfoTimesOut");
    File file = new File(getResourceSourcePath(), "dance1.avi");

    /*
     * Pointed at a process that never answers rather than at a real ffmpeg given an impossibly
     * short deadline. Racing a real probe made this test decide the winner by how loaded the
     * machine was, and it lost often enough on CI to fail the build. Here the watchdog is the only
     * thing that can end the call, so the outcome does not depend on timing at all.
     */
    MultimediaObject instance = new MultimediaObject(file, neverAnsweringLocator());

    long start = System.currentTimeMillis();
    EncoderException thrown =
        assertThrows(EncoderException.class, () -> instance.getInfo(250L), "Expected to time out");
    long elapsed = System.currentTimeMillis() - start;

    assertTrue(
        thrown.getMessage().contains("Gave up reading the media information"),
        "Expected a timeout message, got: " + thrown.getMessage());

    /*
     * The deadline has to actually end the call, not merely be reported once the process finishes
     * by itself. ProcessWrapper.destroy() used to close the streams before killing the process,
     * and closing a pipe does not wake a thread already blocked reading it, so this returned only
     * when the process exited on its own, 120 seconds later, with the right message and entirely
     * the wrong behaviour. The bound is loose because it only needs to tell those two apart.
     */
    assertTrue(
        elapsed < 30000L,
        "Timed out only after the process ended by itself, taking " + elapsed + "ms");
  }

  /**
   * The call must not be ended by the watchdog when the process answers in time, so that a timeout
   * is only ever reported when there really was one.
   */
  @Test
  public void testGetInfoDoesNotTimeOutWhenTheProcessAnswers() throws Exception {
    System.out.println("testGetInfoDoesNotTimeOutWhenTheProcessAnswers");
    File file = new File(getResourceSourcePath(), "dance1.avi");

    MultimediaInfo result = new MultimediaObject(file).getInfo(60000L);

    assertNotNull(result);
    assertEquals("avi", result.getFormat());
  }

  /**
   * A locator for a process that starts, says nothing and does not exit, standing in for an ffmpeg
   * that hangs. It runs {@link NeverFinishes} on the JVM running the tests, so it needs nothing
   * installed and behaves the same on every platform.
   */
  private ProcessLocator neverAnsweringLocator() {
    String javaBin =
        System.getProperty("java.home")
            + File.separator
            + "bin"
            + File.separator
            + (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win")
                ? "java.exe"
                : "java");

    String classpath;
    try {
      classpath =
          new File(
                  NeverFinishes.class
                      .getProtectionDomain()
                      .getCodeSource()
                      .getLocation()
                      .toURI())
              .getAbsolutePath();
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Could not locate the test classes", e);
    }

    return new ProcessLocator() {
      @Override
      public String getExecutablePath() {
        return javaBin;
      }

      @Override
      public ProcessWrapper createExecutor() {
        ProcessWrapper wrapper = new ProcessWrapper(javaBin);
        wrapper.addArgument("-cp");
        wrapper.addArgument(classpath);
        wrapper.addArgument(NeverFinishes.class.getName());
        // getInfo appends "-i <file>" after these, which NeverFinishes ignores.
        return wrapper;
      }
    };
  }

  /** Test of getFile method, of class MultimediaObject. */
  @Test
  public void testGetFile() {
    System.out.println("getFile");
    File file = new File(getResourceSourcePath(), "dance1.avi");
    MultimediaObject instance = new MultimediaObject(file);
    File result = instance.getFile();
    assertEquals(file, result);
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo01() throws Exception {
    System.out.println("testGetInfo01");
    File file = new File(getResourceSourcePath(), "dance1.avi");
    MultimediaObject instance = new MultimediaObject(file);
    MultimediaInfo result = instance.getInfo();

    assertEquals("avi", result.getFormat());
    assertEquals(1530, result.getDuration());
    assertNull(result.getAudio());
    assertEquals("rawvideo", result.getVideo().getDecoder());
    assertEquals(320, result.getVideo().getSize().getWidth());
    assertEquals(240, result.getVideo().getSize().getHeight());
    assertEquals(4817000, result.getVideo().getBitRate());
    assertEquals(15.0f, result.getVideo().getFrameRate());
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo02() throws Exception {
    System.out.println("testGetInfo02");
    File file = new File(getResourceSourcePath(), "4channels.ogg");
    MultimediaObject instance = new MultimediaObject(file);
    MultimediaInfo result = instance.getInfo();

    assertEquals("ogg", result.getFormat());
    assertEquals(20000, result.getDuration());
    assertNull(result.getVideo());
    assertEquals("vorbis", result.getAudio().getDecoder());
    assertEquals(48000, result.getAudio().getSamplingRate());
    assertEquals(4, result.getAudio().getChannels());
    assertEquals(959000, result.getAudio().getBitRate());
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  @Disabled("Info retrieval fails with message: Invalid data found when processing input")
  public void testGetInfo03() throws Exception {
    System.out.println("testGetInfo03");
    File file = new File(getResourceSourcePath(), "2019V7HR.amr");
    MultimediaObject instance = new MultimediaObject(file);

    instance.getInfo();
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo04() throws Exception {
    System.out.println("testGetInfo04");
    assumeRemoteSampleReachable();
    URL source = new URL(REMOTE_SAMPLE);
    MultimediaObject instance = new MultimediaObject(source);
    MultimediaInfo result = instance.getInfo();
    assertEquals("mpeg", result.getFormat(), "Invalid video format");
    // Different ffmpeg releases round the duration of this sample slightly differently,
    // so allow a little slack rather than pin it to the millisecond.
    assertTrue(
        Math.abs(result.getDuration() - 29800L) <= 100L,
        "Invalid duration: " + result.getDuration());
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo06() throws Exception {
    System.out.println("testGetInfo06");
    File file = new File(getResourceSourcePath(), "size1.mp4");
    MultimediaObject instance = new MultimediaObject(file);
    MultimediaInfo result = instance.getInfo();
    assertEquals("mov", result.getFormat(), "Invalid video format");
    assertEquals(640, result.getVideo().getSize().getHeight(), "Video height not as expected");
    assertEquals(360, result.getVideo().getSize().getWidth(), "Video width not as expected");
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo07() throws Exception {
    System.out.println("testGetInfo07");
    File file = new File(getResourceSourcePath(), "size2.mp4");
    MultimediaObject instance = new MultimediaObject(file);
    MultimediaInfo result = instance.getInfo();
    assertEquals("mov", result.getFormat(), "Invalid video format");
    assertEquals(960, result.getVideo().getSize().getHeight(), "Video height not as expected");
    assertEquals(544, result.getVideo().getSize().getWidth(), "Video width not as expected");
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo08() throws Exception {
    System.out.println("testGetInfo08");
    File file = new File(getResourceSourcePath(), "small.mp4");
    MultimediaObject instance = new MultimediaObject(file);
    MultimediaInfo result = instance.getInfo();

    assertEquals("mov", result.getFormat());
    assertEquals(5570, result.getDuration());
    assertNotNull(result.getMetadata());
    assertEquals(4, result.getMetadata().size());
    assertEquals("2010-03-20T21:29:11.000000Z", result.getMetadata().get("creation_time"));
    assertEquals("mp42", result.getMetadata().get("major_brand"));
    assertEquals("0", result.getMetadata().get("minor_version"));
    assertEquals("mp42isomavc1", result.getMetadata().get("compatible_brands"));
    assertNotNull(result.getVideo());
    assertEquals("h264 (Constrained Baseline) (avc1 / 0x31637661)", result.getVideo().getDecoder());
    assertEquals(560, result.getVideo().getSize().getWidth());
    assertEquals(320, result.getVideo().getSize().getHeight());
    assertEquals(465000, result.getVideo().getBitRate());
    assertEquals(30f, result.getVideo().getFrameRate());
    assertNotNull(result.getAudio());
    assertEquals("aac (LC) (mp4a / 0x6134706D)", result.getAudio().getDecoder());
    assertEquals(48000, result.getAudio().getSamplingRate());
    assertEquals(1, result.getAudio().getChannels());
    assertEquals(83000, result.getAudio().getBitRate());
  }

  /**
   * Test of getInfo method, of class MultimediaObject.
   * Test reading video and audio metadata
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfo09() throws Exception {
    System.out.println("testGetInfo09");
    File file = new File("src/test/resources/small.mp4");
    MultimediaObject instance = new MultimediaObject(file);
    MultimediaInfo result = instance.getInfo();

    assertNotNull(result.getVideo());

    VideoInfo videoInfo = result.getVideo();

    assertNotNull(videoInfo.getMetadata());
    assertNotNull(videoInfo.getMetadata().get("creation_time"));

    assertNotNull(result.getAudio());

    AudioInfo audioInfo = result.getAudio();

    assertNotNull(audioInfo.getMetadata());
    assertNotNull(audioInfo.getMetadata().get("creation_time"));
  }
}
