/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/** @author andre */
public class ScreenExtractorTest extends AMediaTest {

  public ScreenExtractorTest() {
    super(null, "ScreenExtractor");
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderImages_01() throws Exception {
    System.out.println("render images 01");
    URL source = new URL("https://samples.ffmpeg.org/MPEG1/zelda%20first%20commercial.mpeg");
    Path target = Paths.get(getResourceTargetPath(), "extractor01");
    clearDirectory(target);

    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = 100;
    int height = 100;
    int seconds = 2;
    String fileNamePrefix = "extractor01";
    String extension = "jpg";
    int quality = 0;
    ScreenExtractor instance = new ScreenExtractor();
    instance.render(multimediaObject, width, height, seconds, target.toFile(), fileNamePrefix, extension, quality);
    long totalFiles = countFiles(target);

    assertEquals(
            instance.getNumberOfScreens(),
            totalFiles,
            "Not correct number of output files, expecting: "
                    + instance.getNumberOfScreens()
                    + " got: "
                    + totalFiles);
    assertEquals(15, totalFiles, "Not 15 output files, but " + totalFiles);
  }

  private static void clearDirectory(Path directory) throws IOException {
    if (Files.isDirectory(directory)) {
      try (Stream<Path> files = Files.list(directory)) {
        for (Path filePath : files.collect(toList())) {
          Files.delete(filePath);
        }

        Files.delete(directory);
      }
    }
  }

  private static long countFiles(Path directory) throws IOException {
    try (Stream<Path> outputFiles = Files.list(directory)) {
      return outputFiles.count();
    }
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderImages_02() throws Exception {
    System.out.println("render images 02");
    File source = new File(getResourceSourcePath(), "AV36_1.AVI");
    Path target = Paths.get(getResourceTargetPath(), "extractor02");
    clearDirectory(target);

    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = 100;
    int height = 100;
    int seconds = 2;
    String fileNamePrefix = "extractor02";
    String extension = "jpg";
    int quality = 0;
    ScreenExtractor instance = new ScreenExtractor();
    instance.render(multimediaObject, width, height, seconds, target.toFile(), fileNamePrefix, extension, quality);
    long totalFiles = countFiles(target);

    assertEquals(
        instance.getNumberOfScreens(),
        totalFiles,
        "Not correct number of output files, expecting: "
            + instance.getNumberOfScreens()
            + " got: "
            + totalFiles);
    assertEquals(16, totalFiles, "Not 16 output files, but " + totalFiles);
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderImages_03() throws Exception {
    System.out.println("render images 03");
    File source = new File(getResourceSourcePath(), "zelda first commercial.mpeg");
    Path target = Paths.get(getResourceTargetPath(), "extractor03");
    clearDirectory(target);

    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = 100;
    int height = 100;
    int seconds = 2;
    String fileNamePrefix = "extractor03";
    String extension = "jpg";
    int quality = 0;
    ScreenExtractor instance = new ScreenExtractor();
    instance.render(multimediaObject, width, height, seconds, target.toFile(), fileNamePrefix, extension, quality);
    long totalFiles = countFiles(target);

    assertEquals(
        instance.getNumberOfScreens(),
        totalFiles,
        "Not correct number of output files, expecting: "
            + instance.getNumberOfScreens()
            + " got: "
            + totalFiles);
    assertEquals(15, totalFiles, "Not 15 output files, but " + totalFiles);
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderImage_01() throws Exception {
    System.out.println("render image 01");
    URL source = new URL("https://samples.ffmpeg.org/MPEG1/zelda%20first%20commercial.mpeg");
    File target = new File(getResourceTargetPath(), "extractor01.jpg");
    Files.deleteIfExists(target.toPath());
    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = 100;
    int height = 100;
    int seconds = 2;
    int quality = 0;
    ScreenExtractor instance = new ScreenExtractor();
    instance.render(multimediaObject, width, height, seconds, target, quality);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderImage_02() throws Exception {
    System.out.println("render image 02");
    URL source = new URL("https://samples.ffmpeg.org/MPEG1/zelda%20first%20commercial.mpeg");
    File target = new File(getResourceTargetPath(), "extractor02.jpg");
    Files.deleteIfExists(target.toPath());
    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = 100;
    int height = 100;
    int seconds = 2;
    int quality = 0;
    ScreenExtractor instance = new ScreenExtractor();
    instance.render(multimediaObject, width, height, seconds, target, quality);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderOneImage00() throws Exception {
    System.out.println("render one image 00");
    File source = new File(getResourceSourcePath(), "zelda first commercial.mpeg");
    File target = new File(getResourceTargetPath(), "RenderOneImage00.jpg");
    Files.deleteIfExists(target.toPath());
    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = -1;
    int height = -1;
    int millis = 15000;
    int quality = 1;
    ScreenExtractor instance = new ScreenExtractor();
    instance.renderOneImage(multimediaObject, width, height, millis, target, quality);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderOneImage01() throws Exception {
    System.out.println("render one image 01");
    File source = new File(getResourceSourcePath(), "zelda first commercial.mpeg");
    File target = new File(getResourceTargetPath(), "RenderOneImage01.jpg");
    Files.deleteIfExists(target.toPath());
    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = 60;
    int height = 60;
    int millis = 15000;
    int quality = 1;
    ScreenExtractor instance = new ScreenExtractor();
    instance.renderOneImage(multimediaObject, width, height, millis, target, quality);
    assertTrue(target.exists(), "Output file missing");
  }

  /**
   * Test of render method, of class ScreenExtractor.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testRenderOneImage03() throws Exception {
    System.out.println("render one image 03");
    File source = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "RenderOneImage03.jpg");
    Files.deleteIfExists(target.toPath());
    MultimediaObject multimediaObject = new MultimediaObject(source);
    int width = -1;
    int height = -1;
    int millis = 56000;
    int quality = 1;
    ScreenExtractor instance = new ScreenExtractor();
    instance.renderOneImage(multimediaObject, width, height, millis, target, quality, true);
    assertTrue(target.exists(), "Output file missing");
  }
}
