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
package ws.schild.jave.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ws.schild.jave.AMediaTest;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filtergraphs.OverlayWatermark;
import ws.schild.jave.filtergraphs.TrimAndWatermark;
import ws.schild.jave.filtergraphs.TrimFadeAndWatermark;
import ws.schild.jave.filters.helpers.Color;
import ws.schild.jave.filters.helpers.FadeDirection;
import ws.schild.jave.filters.helpers.OverlayLocation;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.utils.AutoRemoveableFile;

/** @author a.schild */
public class VideoFilterTest extends AMediaTest {

  private static ClassLoader cLoader = VideoFilterTest.class.getClassLoader();

  public VideoFilterTest() {
    super(null, "VideoFilterTest");
  }

  @Test
  public void testVideoFilter1() throws Exception {
    System.out.println("testVideoFilter1");

    File source = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "testVideoFilter1.mp4");
    if (target.exists()) {
      target.delete();
    }
    DrawtextFilter vf =
        new DrawtextFilter("testVideoFilter1", 30, 30, "Arial", null, 30, new Color("ffffff"));
    vf.setShadow(new Color("000000"), 2, 2);
    VideoAttributes videoAttributes = new VideoAttributes();
    videoAttributes.addFilter(vf);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setVideoAttributes(videoAttributes);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  @Test
  public void testVideoFilter2() throws Exception {
    System.out.println("testVideoFilter2");

    File source = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "testVideoFilter2.mp4");
    if (target.exists()) {
      target.delete();
    }
    DrawtextFilter vf =
        new DrawtextFilter("testVideoFilter2", 30, 30, "Arial", null, 30, new Color("ffffff", "44"));
    vf.setShadow(new Color("000000", "44"), 2, 2);
    VideoAttributes videoAttributes = new VideoAttributes();
    videoAttributes.addFilter(vf);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setVideoAttributes(videoAttributes);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  @Test
  public void testVideoFilter3() throws Exception {
    System.out.println("testVideoFilter3");

    File source = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "testVideoFilter3.mp4");
    if (target.exists()) {
      target.delete();
    }
    DrawtextFilter vf =
        new DrawtextFilter(
            "testVideoFilter3 <[]:=,> End of special chars",
            30,
            30,
            "Arial",
            null,
            30,
            new Color("ffffff", "dd"));
    vf.setShadow(new Color("000000", "44"), 2, 2);
    VideoAttributes videoAttributes = new VideoAttributes();
    videoAttributes.addFilter(vf);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setVideoAttributes(videoAttributes);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  @Test
  public void testVideoFilter4() throws Exception {
    System.out.println("testVideoFilter4");

    File source = new File(getResourceSourcePath(), "testfile3.wmv");
    File target = new File(getResourceTargetPath(), "testVideoFilter4.mp4");
    if (target.exists()) {
      target.delete();
    }
    DrawtextFilter vf =
        new DrawtextFilter(
            "testVideoFilter4 center", -1, -1, "Arial", null, 30, new Color("ffffff", "44"));
    vf.setAddArgument("x=(w-text_w)/2:y=(h-text_h)/2");
    vf.setShadow(new Color("000000", "44"), 2, 2);
    VideoAttributes videoAttributes = new VideoAttributes();
    videoAttributes.addFilter(vf);
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setVideoAttributes(videoAttributes);
    Encoder encoder = new Encoder();
    encoder.encode(new MultimediaObject(source), target, attrs);
    assertTrue(target.exists(), "Output file missing");
  }

  @Test
  public void testOverlayWatermarkExpression() {
    File fooPng = new File("foo.png");
    String fooPath = fooPng.getAbsolutePath();
    OverlayWatermark checkMe = new OverlayWatermark(fooPng, OverlayLocation.BOTTOM_RIGHT, -10, -10);
    assertEquals(
        "movie='"
            + fooPath
            + "'[watermark];[0:v][watermark]overlay='main_w-overlay_w-10:main_h-overlay_h-10'",
        checkMe.getExpression());

    checkMe = new OverlayWatermark(fooPng, OverlayLocation.TOP_LEFT, null, null);
    assertEquals(
        "movie='" + fooPath + "'[watermark];[0:v][watermark]overlay='0:0'",
        checkMe.getExpression());

    checkMe = new OverlayWatermark(fooPng, OverlayLocation.TOP_RIGHT, null, 10);
    assertEquals(
        "movie='" + fooPath + "'[watermark];[0:v][watermark]overlay='main_w-overlay_w:10'",
        checkMe.getExpression());
  }

  @Test
  public void thatWeCanOverlayAWatermark() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4").getFile());
    File watermark = new File(cLoader.getResource("watermark.png").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new OverlayWatermark(watermark, OverlayLocation.BOTTOM_RIGHT, -10, -10));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "overlay.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
  
  @Test
  public void thatWeCanFadeAVideo() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new FadeFilter(FadeDirection.OUT, 1.0, 1.0));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "fade.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }

  @Test
  public void thatTrimAndWatermarkFilterProducesCorrectFiltergraphs() throws Exception {
    List<File> videos =
        Arrays.asList(
                "9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4",
                "A0EF94F6-F922-4676-B767-A600F2E87F53.mp4",
                "B3111BAF-A516-48EC-99FB-B492EB23155D.mp4")
            .stream()
            .map(cLoader::getResource)
            .map(URL::getFile)
            .map(File::new)
            .collect(Collectors.toList());

    List<TrimAndWatermark.TrimInfo> trimInfo =
        videos
            .stream()
            .map(f -> new TrimAndWatermark.TrimInfo(0.5, 1.0))
            .collect(Collectors.toList());
    File fooPng = new File("foo.png");
    String fooPath = fooPng.getAbsolutePath();

    assertEquals(
          "[0]trim='duration=1.0:start=0.5',setpts='PTS-STARTPTS'[filtered0];"
        + "[1]trim='duration=1.0:start=0.5',setpts='PTS-STARTPTS'[filtered1];"
        + "[2]trim='duration=1.0:start=0.5',setpts='PTS-STARTPTS'[filtered2];"
        + "[filtered0][filtered1][filtered2]concat='n=3'[concatenated];"
        + "movie='" + fooPath + "',[concatenated]overlay='main_w-overlay_w-10:main_h-overlay_h-10'",
        new TrimAndWatermark(fooPng, trimInfo).getExpression());
  }

  @Test
  public void thatWeCanTrimAndWatermarkFiles() throws Exception {
    File watermark =
        new File(VideoFilterTest.class.getClassLoader().getResource("watermark.png").getFile());

    List<File> videos =
        Arrays.asList(
                "9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4",
                "A0EF94F6-F922-4676-B767-A600F2E87F53.mp4",
                "B3111BAF-A516-48EC-99FB-B492EB23155D.mp4")
            .stream()
            .map(cLoader::getResource)
            .map(URL::getFile)
            .map(File::new)
            .collect(Collectors.toList());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.setComplexFiltergraph(
        new TrimAndWatermark(
            watermark,
            videos
                .stream()
                .map(v -> new TrimAndWatermark.TrimInfo(0.5, 1.0))
                .collect(Collectors.toList())));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(videos.get(0).getParentFile(), "overlay.mp4")) {
      new Encoder()
          .encode(
              videos.stream().map(MultimediaObject::new).collect(Collectors.toList()),
              target,
              encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }

  @Test
  public void thatWeCanTrimFadeAndWatermarkFiles() throws Exception {
    File watermark =
        new File(VideoFilterTest.class.getClassLoader().getResource("watermark.png").getFile());

    List<File> videos =
        Arrays.asList(
                "9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4",
                "A0EF94F6-F922-4676-B767-A600F2E87F53.mp4",
                "B3111BAF-A516-48EC-99FB-B492EB23155D.mp4")
            .stream()
            .map(cLoader::getResource)
            .map(URL::getFile)
            .map(File::new)
            .collect(Collectors.toList());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.setComplexFiltergraph(
        new TrimFadeAndWatermark(
            watermark,
            videos
                .stream()
                .map(v -> new TrimAndWatermark.TrimInfo(0.5, 1.0))
                .collect(Collectors.toList())));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(videos.get(0).getParentFile(), "overlay.mp4")) {
      new Encoder()
          .encode(
              videos.stream().map(MultimediaObject::new).collect(Collectors.toList()),
              target,
              encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }

  @Test
  public void thatWeCanPadAndZoomImages() throws Exception {
    File profileSample =
        new File(VideoFilterTest.class.getClassLoader().getResource("profileSample.png").getFile());
    VideoSize profileSize = new VideoSize(1532, 1378);
    
    VideoSize outputSize = new VideoSize(1280, 720);
    
    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.setComplexFiltergraph(new FilterGraph(new FilterChain(
        new PadFilter(outputSize),
        new ZoomPanFilter(8*25, profileSize, outputSize)
        )));
    vidAttr.setPixelFormat("yuv420p");
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(profileSample.getParentFile(), "zoompan.mp4")) {
      new Encoder()
          .encode(
              new MultimediaObject(profileSample),
              target,
              encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
}
