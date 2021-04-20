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
public class AssSubtitlesFilterTest extends AMediaTest {
  private static final ClassLoader cLoader = AssSubtitlesFilterTest.class.getClassLoader();

  public AssSubtitlesFilterTest() {
    super(null, "AssSubtitlesFilterTest");
  }

  @Test
  public void testAssSubtitle() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4").getFile());
    File assSubtitle = new File(cLoader.getResource("test.ass").getFile());

    VideoAttributes vidAttr = new VideoAttributes();
    vidAttr.addFilter(new AssSubtitlesFilter(assSubtitle));
    EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr);

    try (AutoRemoveableFile target =
        new AutoRemoveableFile(sourceVideo.getParentFile(), "ass-subtitle.mp4")) {
      new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
      assertTrue(target.exists(), "Output file missing");
    }
  }
    
}
