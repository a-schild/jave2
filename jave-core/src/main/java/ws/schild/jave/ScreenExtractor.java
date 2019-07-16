package ws.schild.jave;


import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenExtractor.class);

    /**
     * The locator of the ffmpeg executable used by this extractor.
     */
    private final FFMPEGLocator locator;
    private int numberOfScreens;

    /**
     * It builds an extractor using a {@link DefaultFFMPEGLocator} instance to
     * locate the ffmpeg executable to use.
     */
    public ScreenExtractor() {
        this.locator = new DefaultFFMPEGLocator();
    }

    /**
     *
     * @return The number of screens found
     */
    public int getNumberOfScreens() {
        return numberOfScreens;
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
     * @param fileNamePrefix Name all thumbnails will start with
     * @param extension Image extension for output (jpg, png, etc)
     * @param quality The range is between 1-31 with 31 being the worst quality
     * @throws InputFormatException If the source multimedia file cannot be
     * decoded.
     * @throws EncoderException If a problems occurs during the encoding
     * process.
     */
    public void render(MultimediaObject multimediaObject, int width, int height, int seconds, File outputDir,
            String fileNamePrefix, String extension, int quality)
            throws InputFormatException, EncoderException {
        String inputSource = multimediaObject.isURL() ? multimediaObject.getURL().toString() : multimediaObject.getFile().getAbsolutePath();
        try
        {
            if (!outputDir.exists())
            {
                if (!outputDir.mkdirs())
                {
                    LOG.debug("Failed to create destination folder");
                    throw new SecurityException();
                }
            }
            if (!multimediaObject.isURL() && !multimediaObject.getFile().canRead())
            {
                LOG.debug("Failed to open input file");
                throw new SecurityException();
            }
        } catch (SecurityException e)
        {
            LOG.debug("Access denied checking destination folder",  e);
        }

        MultimediaInfo multimediaInfo = multimediaObject.getInfo();
        numberOfScreens = Math.round(((float)multimediaInfo.getDuration()) / 1000.0f / seconds);

        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(inputSource);
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

        try
        {
            ffmpeg.execute();
        } catch (IOException e)
        {
            throw new EncoderException(e);
        }
        try
        {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null)
            {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e)
        {
            throw new EncoderException(e);
        } finally
        {
            ffmpeg.destroy();
        }

    }

    /**
     * Generate a single screenshot from source video.
     *
     * @param multimediaObject Source MultimediaObject @see MultimediaObject
     * @param width Output width
     * @param height Output height
     * @param seconds Interval in seconds between screens
     * @param target Destination of output image
     * @param quality The range is between 1-31 with 31 being the worst quality
     * @throws InputFormatException If the source multimedia file cannot be
     * decoded.
     * @throws EncoderException If a problems occurs during the encoding
     * process.
     */
    public void render(MultimediaObject multimediaObject, int width, int height, int seconds, File target, int quality)
            throws EncoderException {
        String inputSource = multimediaObject.isURL() ? multimediaObject.getURL().toString() : multimediaObject.getFile().getAbsolutePath();
        target = target.getAbsoluteFile();
        target.getParentFile().mkdirs();
        try
        {
            if (!multimediaObject.isURL() && !multimediaObject.getFile().canRead())
            {
                LOG.debug("Failed to open input file");
                throw new SecurityException();
            }
        } catch (SecurityException e)
        {
            LOG.debug("Access denied checking destination folder", e);
        }

        MultimediaInfo multimediaInfo = multimediaObject.getInfo();
        int duration = (int) (multimediaInfo.getDuration() / 1000);
        numberOfScreens = seconds <= duration ? 1 : 0;

        FFMPEGExecutor ffmpeg = this.locator.createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(inputSource);
        ffmpeg.addArgument("-f");
        ffmpeg.addArgument("image2");
        ffmpeg.addArgument("-vframes");
        ffmpeg.addArgument("1");
        ffmpeg.addArgument("-ss");
        ffmpeg.addArgument(String.valueOf(seconds));
        ffmpeg.addArgument("-s");
        ffmpeg.addArgument(String.format("%sx%s", String.valueOf(width), String.valueOf(height)));
        ffmpeg.addArgument("-qscale");
        ffmpeg.addArgument(String.valueOf(quality));
        ffmpeg.addArgument(target.getAbsolutePath());

        try
        {
            ffmpeg.execute();
        } catch (IOException e)
        {
            throw new EncoderException(e);
        }
        try
        {
            RBufferedReader reader = new RBufferedReader(
                    new InputStreamReader(ffmpeg.getErrorStream()));
            int step = 0;
            int lineNR = 0;
            String line;
            while ((line = reader.readLine()) != null)
            {
                lineNR++;
                LOG.debug("Input Line ({}): {}", lineNR, line);
                // TODO: Implement additional input stream parsing
            }
        } catch (IOException e)
        {
            throw new EncoderException(e);
        } finally
        {
            ffmpeg.destroy();
        }

    }

}
