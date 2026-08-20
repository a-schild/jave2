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
package ws.schild.jave.smoke;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

/**
 * Proves that jave works when compiled ahead of time by GraalVM native-image (#276).
 *
 * <p>The interesting part is the very first step. The bundled ffmpeg is a resource inside the
 * nativebin jar, and native-image discards resources unless something registers them, so without
 * the {@code resource-config.json} shipped in those jars this fails with "Could not find ffmpeg
 * platform executable in resources", which is exactly what #276 reported.
 *
 * <p>Run as a normal java program it should also pass, which makes it useful for telling a
 * native-image problem apart from an ordinary one.
 */
public class GraalVmSmokeTest {

  public static void main(String[] args) throws Exception {
    File workDir = new File(args.length > 0 ? args[0] : ".");

    step("locating the bundled ffmpeg");
    String executable = new DefaultFFMPEGLocator().getExecutablePath();
    if (executable == null) {
      fail("the locator returned no path");
    }
    File binary = new File(executable);
    if (!binary.isFile() || binary.length() == 0) {
      fail("the executable was not extracted to " + executable);
    }
    System.out.println("    extracted " + executable + " (" + binary.length() + " bytes)");

    step("asking it what it can encode");
    String[] encoders = new Encoder().getAudioEncoders();
    if (encoders == null || encoders.length == 0) {
      fail("no audio encoders reported, so the process did not run");
    }
    System.out.println("    " + encoders.length + " audio encoders");

    step("encoding something");
    File source = new File(workDir, "smoke-source.wav");
    File target = new File(workDir, "smoke-target.mp3");
    generateSilence(source);

    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    audio.setBitRate(64000);
    audio.setChannels(1);
    audio.setSamplingRate(44100);

    EncodingAttributes attrs = new EncodingAttributes();
    attrs.setOutputFormat("mp3");
    attrs.setAudioAttributes(audio);

    new Encoder().encode(new MultimediaObject(source), target, attrs);
    if (!target.isFile() || target.length() == 0) {
      fail("nothing was encoded to " + target);
    }
    System.out.println("    wrote " + target.getName() + " (" + target.length() + " bytes)");

    step("reading it back");
    MultimediaInfo info = new MultimediaObject(target).getInfo();
    if (info == null || info.getAudio() == null) {
      fail("the result could not be read back");
    }
    System.out.println("    format " + info.getFormat() + ", " + info.getAudio().getDecoder());

    System.out.println();
    System.out.println("GraalVM smoke test passed");
  }

  /**
   * Half a second of silence as a 16 bit mono wav, written by hand so that the smoke test carries
   * no fixture file and does not depend on ffmpeg to produce its own input.
   */
  private static void generateSilence(File target) throws IOException {
    int sampleRate = 44100;
    int samples = sampleRate / 2;
    int dataBytes = samples * 2;

    ByteBuffer wav = ByteBuffer.allocate(44 + dataBytes).order(ByteOrder.LITTLE_ENDIAN);
    wav.put("RIFF".getBytes(StandardCharsets.US_ASCII));
    wav.putInt(36 + dataBytes);
    wav.put("WAVE".getBytes(StandardCharsets.US_ASCII));
    wav.put("fmt ".getBytes(StandardCharsets.US_ASCII));
    wav.putInt(16); // pcm header size
    wav.putShort((short) 1); // pcm, uncompressed
    wav.putShort((short) 1); // mono
    wav.putInt(sampleRate);
    wav.putInt(sampleRate * 2); // byte rate
    wav.putShort((short) 2); // block align
    wav.putShort((short) 16); // bits per sample
    wav.put("data".getBytes(StandardCharsets.US_ASCII));
    wav.putInt(dataBytes);
    // the samples themselves are already zero, which is silence

    Files.write(target.toPath(), wav.array());
  }

  private static void step(String what) {
    System.out.println("==> " + what);
  }

  private static void fail(String why) {
    System.err.println("GraalVM smoke test FAILED: " + why);
    System.exit(1);
  }
}
