package ws.schild.jave;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ws.schild.jave.progress.EchoingProgressListener;

public class ConcatDemuxerTest {

	public static VideoProcessor vps;
	
	@Test
	public void thatWeCanMergeFiles() throws Exception {
		VideoProcessor vps = new VideoProcessor();
		File videoDirectory = new File("/Users/mressler/Downloads/Mikey/");
		List<File> videos = Arrays.asList(videoDirectory.listFiles((d, s) -> s.endsWith("mp4")));
		
		File destination = new File("/Users/mressler/Downloads/MikeyMerge.mp4");
		
		vps.catClipsTogether(videos, destination, new EchoingProgressListener("Test Merge"));
	}
	
}
