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
package ws.schild.jave;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

/**
 * What happens to the album art when converting audio, which surprises people.
 *
 * <p>Cover art is carried as a video stream with an attached picture disposition. An encoding with
 * no {@link VideoAttributes} gets {@code -vn} added, meaning no video, so the picture goes with it.
 * That is correct for what was asked and rarely what was wanted.
 *
 * <p>These record both halves so the behaviour is written down rather than rediscovered: that
 * audio only conversion drops the art, and that copying the video stream keeps it.
 */
public class CoverArtTest extends AMediaTest {

  public CoverArtTest() {
    super(null, "CoverArt");
  }

  /**
   * Audio only conversion drops the art, because -vn is what audio only means.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testAudioOnlyConversionDropsTheCoverArt() throws Exception {
    File source = flacWithCoverArt("dropped.flac");
    File target = new File(getResourceTargetPath(), "dropped.mp3");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
    // No video attributes, so the encoder adds -vn

    new Encoder().encode(new MultimediaObject(source), target, attrs);

    MultimediaInfo info = new MultimediaObject(target).getInfo();
    assertNull(
        info.getVideo(),
        "The cover survived an audio only conversion, which means -vn stopped being added");
  }

  /**
   * Copying the video stream keeps it. This is the answer to give anyone who asks.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testCopyingTheVideoStreamKeepsTheCoverArt() throws Exception {
    File source = flacWithCoverArt("kept.flac");
    File target = new File(getResourceTargetPath(), "kept.mp3");
    Files.deleteIfExists(target.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");

    // The whole trick. Asking for video, and asking for it unchanged, keeps the
    // attached picture instead of -vn throwing it away.
    VideoAttributes video = new VideoAttributes();
    video.setCodec("copy");

    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);
    attrs.setVideoAttributes(video);

    new Encoder().encode(new MultimediaObject(source), target, attrs);

    MultimediaInfo info = new MultimediaObject(target).getInfo();
    assertNotNull(info.getAudio(), "The audio went missing");
    assertNotNull(
        info.getVideo(),
        "The cover was not carried over, copying the video stream is supposed to keep it");
  }

  /**
   * Builds a flac carrying an embedded cover, since the test resources have none. Uses the bundled
   * ffmpeg directly, which keeps a binary fixture out of the repository.
   *
   * @param name What to call it.
   * @return The file.
   * @throws java.lang.Exception If it could not be built.
   */
  private File flacWithCoverArt(String name) throws Exception {
    File audio = new File(getResourceSourcePath(), "Alesis-Fusion-Clean-Guitar-C3.wav");
    File picture = new File(getResourceSourcePath(), "watermark.png");
    File out = new File(getResourceTargetPath(), name);
    Files.deleteIfExists(out.toPath());

    ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
    for (String arg :
        new String[] {
          "-i", audio.getAbsolutePath(),
          "-i", picture.getAbsolutePath(),
          "-map", "0:a",
          "-map", "1:v",
          "-c:a", "flac",
          "-c:v", "copy",
          "-disposition:v", "attached_pic",
          "-y", out.getAbsolutePath()
        }) {
      ffmpeg.addArgument(arg);
    }
    try {
      ffmpeg.execute();
      // drain, or the process can block on a full pipe
      java.io.InputStream err = ffmpeg.getErrorStream();
      while (err.read() != -1) {
        // discard
      }
      ffmpeg.getProcessExitCode();
    } finally {
      ffmpeg.destroy();
    }

    assertTrue(out.exists() && out.length() > 0, "Could not build a flac carrying a cover");
    assertNotNull(
        new MultimediaObject(out).getInfo().getVideo(), "The fixture has no cover to lose");
    return out;
  }
}
