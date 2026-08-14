/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

/** @author a.schild */
public abstract class AMediaTest {

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
