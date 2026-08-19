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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.junit.jupiter.api.Test;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;

/**
 * Holds the line that CVE-2023-48909 claims is not there.
 *
 * <p>The report says this library "does not filter the input commands and can execute arbitrary
 * commands". No shell is involved anywhere: every process is started through {@code
 * Runtime.exec(String[])}, which hands the arguments to the operating system as a list rather than
 * to a command interpreter, so shell syntax in a file name is a file name and nothing else. These
 * tests are here so that stays true, since the day someone builds a command line by pasting strings
 * together is the day it stops being.
 *
 * <p>What the report describes is an application handing user input to {@link
 * ws.schild.jave.process.ProcessWrapper#addArgument(String)}, which is a method for adding ffmpeg
 * arguments and will faithfully add whatever it is given. That is worth documenting, and is, but it
 * is not something the library can filter without ceasing to be a wrapper around ffmpeg.
 */
public class ShellMetacharacterTest extends AMediaTest {

  public ShellMetacharacterTest() {
    super(null, "ShellMetacharacter");
  }

  /**
   * A name made of shell syntax. Semicolon, ampersand, dollar, backtick and pipe are all legal in
   * file names on both windows and unix, which is exactly why they are worth testing.
   */
  private static final String HOSTILE_NAME = "inj;&$(id)`id`&&whoami.avi";

  /** The file a command interpreter would leave behind if one were ever involved. */
  private static final String MARKER = "pwned-marker";

  /**
   * Reading a file whose name is full of shell syntax has to work, and has to leave nothing behind.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testGetInfoOnFileNamedLikeAShellCommand() throws Exception {
    File hostile = copyOfDanceNamed(HOSTILE_NAME);
    File marker = new File(getResourceTargetPath(), MARKER);
    Files.deleteIfExists(marker.toPath());

    MultimediaInfo info = new MultimediaObject(hostile).getInfo();

    assertNotNull(info, "Nothing came back for a file with an awkward name");
    assertEquals("avi", info.getFormat(), "The name changed how the file was read");
    assertFalse(
        marker.exists(),
        "Something executed the file name instead of reading it, which would mean a shell is in"
            + " the path somewhere");
  }

  /**
   * The same for encoding, where the name reaches ffmpeg as the output argument rather than the
   * input, and so is the more interesting of the two.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeToFileNamedLikeAShellCommand() throws Exception {
    // A source that actually has an audio stream, dance1.avi has none
    File source = new File(getResourceSourcePath(), "Alesis-Fusion-Clean-Guitar-C3.wav");
    File target = new File(getResourceTargetPath(), "out;&$(id)`id`.mp3");
    Files.deleteIfExists(target.toPath());
    File marker = new File(getResourceTargetPath(), MARKER);
    Files.deleteIfExists(marker.toPath());

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);

    new Encoder().encode(new MultimediaObject(source), target, attrs);

    assertTrue(target.exists(), "Nothing was written to a target with an awkward name");
    assertTrue(target.length() > 0, "The target with an awkward name is empty");
    assertFalse(marker.exists(), "Something executed the target name instead of writing to it");
  }

  /**
   * A name that starts with a dash is the other half of this. No shell is involved, but ffmpeg
   * reads its own command line, and an argument beginning with a dash is an option to it. Paths are
   * made absolute before they are handed over, which is what stops a file called -y from turning
   * into one.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testFileNameStartingWithADashIsNotReadAsAnOption() throws Exception {
    File hostile = copyOfDanceNamed("-y.avi");

    MultimediaInfo info = new MultimediaObject(hostile).getInfo();

    assertNotNull(info, "Nothing came back for a file whose name looks like an ffmpeg option");
    assertEquals("avi", info.getFormat(), "A file called -y was not read as a file");
  }

  private File copyOfDanceNamed(String name) throws Exception {
    File source = new File(getResourceSourcePath(), "dance1.avi");
    File copy = new File(getResourceTargetPath(), name);
    Files.copy(source.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return copy;
  }
}
