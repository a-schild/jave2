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
public class ScaleFilterTest extends AMediaTest {
  private static final ClassLoader cLoader = ScaleFilterTest.class.getClassLoader();

  public ScaleFilterTest() {
    super(null, "ScaleFilterTest");
  }

  @Test
  public void testGifToVideoScaling() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("giphy.gif").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new ScaleFilter("trunc(iw/2)*2:trunc(ih/2)*2"));
    vidAttr.setPixelFormat("yuv420p");
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);
    encAttr.setOutputFormat("mp4");

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "giphy-scaling.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
    
}
