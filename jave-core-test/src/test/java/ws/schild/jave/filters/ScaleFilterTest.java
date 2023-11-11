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
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;
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
  
  @Test
  public void testMpegVideoScaling() throws Exception {
    File sourceVideo =
        new File(cLoader.getResource("small.mp4").getFile());
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);

        VideoAttributes video = new VideoAttributes();
        video.setCodec("h264");
        video.setBitRate(160000);
        video.setFrameRate(30);
        video.setSize(new VideoSize(1280,596));
        EncodingAttributes encAttr = new EncodingAttributes();
        encAttr.setOutputFormat("mp4");
        encAttr.setAudioAttributes(audio);
        encAttr.setVideoAttributes(video);


        try (AutoRemoveableFile target =
            new AutoRemoveableFile(sourceVideo.getParentFile(), "even-scaling.mp4")) {
          new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
          assertTrue(target.exists(), "Output file missing");
        }
  }    
}
