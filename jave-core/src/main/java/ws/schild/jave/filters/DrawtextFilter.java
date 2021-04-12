package ws.schild.jave.filters;

import java.io.File;

import ws.schild.jave.filters.helpers.Color;
import ws.schild.jave.utils.Utils;

/**
 * Add text to a video. An implementation of the drawtext filter from the <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#drawtext-1">FFMPEG Documentation</a>.
 *
 * <p>https://write.corbpie.com/how-to-do-a-text-watermark-in-ffmpeg/ -vf "drawtext=text='a
 * watermark':x=10:y=H-th-10:fontfile=/pathto/font.ttf:fontsize=10:fontcolor=white:shadowcolor=black:shadowx=2:shadowy=2"
 */
public class DrawtextFilter extends Filter {

  /**
   * @param text The text string to be drawn. The text must be a sequence of UTF-8 encoded
   *     characters. This parameter is mandatory if no file is specified with the parameter
   *     textfile.
   * @param posX X Position of watermark text. The expressions which specify the offsets where text
   *     will be drawn within the video frame. They are relative to the top/left border of the
   *     output image. The default value of x and y is "0".
   * @param posY Y Position of watermark text. The expressions which specify the offsets where text
   *     will be drawn within the video frame. They are relative to the top/left border of the
   *     output image. The default value of x and y is "0".
   */
  public DrawtextFilter(
      String text,
      String posX,
      String posY) {
    super("drawtext");
    addNamedArgument("text", Utils.escapeArgument(text));
    addNamedArgument("x", posX);
    addNamedArgument("y", posY);
  }

  /**
   * @param text The text string to be drawn. The text must be a sequence of UTF-8 encoded
   *     characters. This parameter is mandatory if no file is specified with the parameter
   *     textfile.
   * @param posX X Position of watermark text. The expressions which specify the offsets where text
   *     will be drawn within the video frame. They are relative to the top/left border of the
   *     output image. The default value of x and y is "0".
   * @param posY Y Position of watermark text. The expressions which specify the offsets where text
   *     will be drawn within the video frame. They are relative to the top/left border of the
   *     output image. The default value of x and y is "0".
   * @param fontName The font family to be used for drawing text. By default Sans.
   * @param fontSize The font size to be used for drawing text. The default value of fontsize is 16.
   * @param fontColor The color to be used for drawing fonts. The default value of fontcolor is
   *     "black".
   */
  public DrawtextFilter(
      String text,
      String posX,
      String posY,
      String fontName,
      Double fontSize,
      Color fontColor)
      throws IllegalArgumentException {
    this(text, posX, posY);

    addNamedArgument("font", fontName);
    addNamedArgument("fontsize", fontSize.toString());
    addNamedArgument("fontcolor", fontColor.toString());
  }

  /**
   * @param text The text string to be drawn. The text must be a sequence of UTF-8 encoded
   *     characters. This parameter is mandatory if no file is specified with the parameter
   *     textfile.
   * @param posX X Position of watermark text. The expressions which specify the offsets where text
   *     will be drawn within the video frame. They are relative to the top/left border of the
   *     output image. The default value of x and y is "0".
   * @param posY Y Position of watermark text. The expressions which specify the offsets where text
   *     will be drawn within the video frame. They are relative to the top/left border of the
   *     output image. The default value of x and y is "0".
   * @param fontFile The font file to be used for drawing text. The path must be included. This
   *     parameter is mandatory if the fontconfig support is disabled.
   * @param fontSize The font size to be used for drawing text. The default value of fontsize is 16.
   * @param fontColor The color to be used for drawing fonts. The default value of fontcolor is
   *     "black".
   */
  public DrawtextFilter(
      String text,
      String posX,
      String posY,
      File fontFile,
      Double fontSize,
      Color fontColor)
      throws IllegalArgumentException {
    this(text, posX, posY);

    addNamedArgument("fontfile", fontFile.getAbsolutePath());
    addNamedArgument("fontsize", fontSize.toString());
    addNamedArgument("fontcolor", fontColor.toString());
  }

  /**
   * @param shadowColor Color of shadow
   * @param shadowX X Position of shadow, relative to text
   * @param shadowY Y Position of shadow, relative to text
   * @return this instance
   */
  public DrawtextFilter setShadow(Color shadowColor, Integer shadowX, Integer shadowY) {
    addNamedArgument("shadowcolor", shadowColor.toString());
    addNamedArgument("shadowx", shadowX.toString());
    addNamedArgument("shadowy", shadowY.toString());
    return this;
  }

  /**
   * Used to draw a box around text using the background color.
   *
   * @param borderWidth Set the width of the border to be drawn around the box using boxcolor. The
   *     default value of boxborderw is 0.
   * @param color The color to be used for drawing box around text. The default value of boxcolor is
   *     "white".
   * @return this instance.
   */
  public DrawtextFilter setBox(Integer borderWidth, Color color) {
    addNamedArgument("box", "1");
    addNamedArgument("boxcolor", color.toString());
    addNamedArgument("boxborderw", borderWidth.toString());
    return this;
  }

  /**
   * @param borderWidth Set the width of the border to be drawn around the text using bordercolor.
   *     The default value of borderw is 0.
   * @param color Set the color to be used for drawing border around text. The default value of
   *     bordercolor is "black".
   * @return this instance
   */
  public DrawtextFilter setBorder(Integer borderWidth, Color color) {
    addNamedArgument("bordercolor", color.toString());
    addNamedArgument("borderw", borderWidth.toString());
    return this;
  }

  /**
   * @param lineSpacing Set the line spacing in pixels of the border to be drawn around the box
   *     using box. The default value of line_spacing is 0.
   * @return this instance
   */
  public DrawtextFilter setLineSpacing(Integer lineSpacing) {
    addNamedArgument("line_spacing", lineSpacing.toString());
    return this;
  }
}
