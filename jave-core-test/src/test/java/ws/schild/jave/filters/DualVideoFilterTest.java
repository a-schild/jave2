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
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.utils.AutoRemoveableFile;

/**
 *
 * @author a.schild
 */
public class DualVideoFilterTest extends AMediaTest {
  private static final ClassLoader cLoader = DualVideoFilterTest.class.getClassLoader();

  public DualVideoFilterTest() {
    super(null, "DualVideoFilterTest");
  }

  @Test
  public void testAssSubtitle() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4").getFile());
    File assSubtitle = new File(cLoader.getResource("test.ass").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new AssSubtitlesFilter(assSubtitle));
    
    VideoSize size=new VideoSize(1080,720);
        ScaleFilter scaleFilter=new ScaleFilter(size);
        vidAttr.addFilter(scaleFilter);

    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "dualfilter.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
    
}
