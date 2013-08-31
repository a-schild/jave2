package it.sauronsoftware.jave;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScreenExtractor {
    private final static Log _log = LogFactory.getLog(ScreenExtractor.class);

    /**
     * The locator of the ffmpeg executable used by this extractor.
     */
    private FFMPEGLocator locator;

    /**
     * It builds an extractor using a {@link DefaultFFMPEGLocator} instance to
     * locate the ffmpeg executable to use.
     */
    public ScreenExtractor() {
        this.locator = new DefaultFFMPEGLocator();
    }

    /**
     * It builds an extractor with a custom {@link FFMPEGLocator}.
     *
     * @param locator The locator picking up the ffmpeg executable used by the
     * extractor.
     */
    public ScreenExtractor(FFMPEGLocator locator) {
        this.locator = locator;
    }

    /**
     * Generates screenshots from source video.
     *
     * @param multimediaObject Source MultimediaObject @see MultimediaObject
     * @param width Output width
     * @param height Output height
     * @param seconds Interval in seconds between screens
     * @param outputDir Destination of output images
     * @param fileNamePrefix
     * @param extension Image extension for output (jpg, png, etc)
     * @param quality The range is between 1-31 with 31 being the worst quality
     * @throws InputFormatException If the source multimedia file cannot be
     * decoded.
     * @throws EncoderException If a problems occurs during the encoding
     * process.
     */

    public void render (MultimediaObject multimediaObject, int width, int height, int seconds, File outputDir,
                        String fileNamePrefix, String extension, int quality)
            throws InputFormatException, EncoderException {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(multimediaObject.getFile().getAbsolutePath());
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("image2");
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument(String.format("fps=fps=1/%s", String.valueOf(seconds)));
        ffmpeg.addArgument("-s");
        ffmpeg.addArgument(String.format("%sx%s", String.valueOf(width), String.valueOf(height)));
        ffmpeg.addArgument("-qscale");
        ffmpeg.addArgument(String.valueOf(quality));
        ffmpeg.addArgument(String.format("%s%s%s-%%04d.%s",
                outputDir.getAbsolutePath(), File.separator, fileNamePrefix, extension));

        try {
            ffmpeg.execute();
        } catch (IOException e) {
            throw new EncoderException(e);
        }
        try {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNR++;
                _log.debug("Input Line (" + lineNR + "): " + line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e) {
            throw new EncoderException(e);
        } finally {
            ffmpeg.destroy();
        }

    }

}