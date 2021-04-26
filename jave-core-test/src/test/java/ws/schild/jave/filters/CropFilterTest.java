/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave.filters;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import ws.schild.jave.AMediaTest;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.utils.AutoRemoveableFile;

/**
 *
 * @author a.schild
 */
public class CropFilterTest extends AMediaTest {
  private static final ClassLoader cLoader = CropFilterTest.class.getClassLoader();

  public CropFilterTest() {
    super(null, "CropFilterTest");
  }

  @Test
  public void testCropFilter1() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("size2.mp4").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new CropFilter("in_w/2:in_h/2:in_w/2:in_h/2"));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);
    encAttr.setOutputFormat("mp4");

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "crop1-size2.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
    
  @Test
  public void testCropFilter2() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("size2.mp4").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new CropFilter(256, 800, 60, 60));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);
    encAttr.setOutputFormat("mp4");

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "crop2-size2.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
}
