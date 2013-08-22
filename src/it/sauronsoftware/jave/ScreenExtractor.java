package it.sauronsoftware.jave;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public void render (File inputFile, int width, int height, String compression, File outputDir) throws InputFormatException, EncoderException {
        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(inputFile.getAbsolutePath());
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("image2");
        ffmpeg.addArgument("-vf");
        ffmpeg.addArgument(String.valueOf("fps=fps=1/5"));
        ffmpeg.addArgument("-s");
        ffmpeg.addArgument(String.format("%sx%s", String.valueOf(width), String.valueOf(height)));
        ffmpeg.addArgument(outputDir.getAbsolutePath() + "\\test-%04d.png");

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