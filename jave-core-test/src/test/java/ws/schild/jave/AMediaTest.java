/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.net.URL;

/** @author a.schild */
public abstract class AMediaTest {

  /** The sample file the tests that read from an URL work on. */
  protected static final String REMOTE_SAMPLE =
      "https://samples.ffmpeg.org/MPEG1/zelda%20first%20commercial.mpeg";

  /** Probed once per jvm, null until the first test asks for it. */
  private static Boolean remoteSampleReachable = null;

  /**
   * How far the size of an encoded file may deviate from the recorded one, as a fraction of it.
   * Different ffmpeg builds pack the same content slightly differently, so an exact comparison only
   * says which ffmpeg produced the number. The check is here to notice output that is truncated,
   * empty or wildly off, and a few percent of slack does not weaken it for that.
   */
  private static final double SIZE_TOLERANCE = 0.05;

  private final String resourceSourcePath;
  private final String resourceTargetPath;

  /**
   * @param sourcePart
   * @param targetPart
   */
  public AMediaTest(String sourcePart, String targetPart) {
    resourceSourcePath = initialiseDirectory(sourcePart, "src/test/resources/");
    resourceTargetPath = initialiseDirectory(targetPart, "target/testoutput/");
  }

  private String initialiseDirectory(String path, String basePath) {
    String resourcePath;

    if (path == null) {
      resourcePath = basePath;
    } else if (path.endsWith("/")) {
      resourcePath = basePath + path;
    } else {
      resourcePath = basePath + path + "/";
    }

    new File(resourcePath).mkdirs();

    return resourcePath;
  }

  public String getResourceSourcePath() {
    return resourceSourcePath;
  }

  public String getResourceTargetPath() {
    return resourceTargetPath;
  }

  /**
   * Skips the calling test unless the bundled ffmpeg can actually read {@link #REMOTE_SAMPLE}.
   *
   * <p>Plain http reachability is not the condition that matters and is not enough to tell. On
   * github hosted runners the host answers a HEAD request perfectly well, yet the bundled static
   * ffmpeg still fails on the same url, returning nothing parsable or dying on a segmentation
   * fault, exit code 139. So the probe is the operation itself, done once per jvm.
   *
   * <p>A test that cannot get the input in front of the encoder says nothing about this library,
   * so it is reported as skipped rather than failed.
   */
  protected static void assumeRemoteSampleReachable() {
    if (remoteSampleReachable == null) {
      remoteSampleReachable = probeRemoteSample();
    }
    assumeTrue(
        remoteSampleReachable,
        "The bundled ffmpeg cannot read " + REMOTE_SAMPLE + " in this environment");
  }

  private static boolean probeRemoteSample() {
    try {
      return new MultimediaObject(new URL(REMOTE_SAMPLE)).getInfo() != null;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Asserts that an encoded file is about the size it used to be, within {@link #SIZE_TOLERANCE}.
   *
   * @param expectedSize The size recorded when the test was written.
   * @param target The file produced by the encoding under test.
   */
  protected static void assertFileSizeNear(long expectedSize, File target) {
    long actualSize = target.length();
    long tolerated = Math.max(1L, Math.round(expectedSize * SIZE_TOLERANCE));
    long difference = Math.abs(actualSize - expectedSize);

    assertTrue(
        difference <= tolerated,
        () ->
            "Output file size "
                + actualSize
                + " differs from the expected "
                + expectedSize
                + " by "
                + difference
                + " bytes, which is more than the tolerated "
                + tolerated);
  }
}
