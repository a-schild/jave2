package ws.schild.jave;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import ws.schild.jave.progress.EchoingProgressListener;
import ws.schild.jave.utils.AutoRemoveableFile;

public class ConcatDemuxerTest {

  @Test
  public void thatWeCanMergeFiles() throws Exception {
    VideoProcessor vps = new VideoProcessor();
    ClassLoader cLoader = ConcatDemuxerTest.class.getClassLoader();

    List<File> videos =
        Stream.of(
                "9B8CC2D5-3B24-4DD1-B23D-9B5DAF0E70BE.mp4",
                "A0EF94F6-F922-4676-B767-A600F2E87F53.mp4",
                "B3111BAF-A516-48EC-99FB-B492EB23155D.mp4")
            .map(cLoader::getResource)
            .map(URL::getFile)
            .map(File::new)
            .collect(Collectors.toList());

    try (AutoRemoveableFile destination =
        new AutoRemoveableFile(videos.get(0).getParentFile(), "merge.mp4")) {
      vps.catClipsTogether(videos, destination, new EchoingProgressListener("Test Merge"));
      assertTrue(destination.exists(), "Output file missing");
    }
  }
}
