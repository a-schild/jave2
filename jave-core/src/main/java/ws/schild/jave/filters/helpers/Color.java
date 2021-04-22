/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave.filters.helpers;

/**
 * A color object, as <a href="https://ffmpeg.org/ffmpeg-utils.html#color-syntax">FFMPEG
 * documentation</a> spells out.
 */
public class Color {
  private String color;
  private String alpha = "ff";

  public static final Color AliceBlue = new Color("0xF0F8FF");
  public static final Color AntiqueWhite = new Color("0xFAEBD7");
  public static final Color Aqua = new Color("0x00FFFF");
  public static final Color Aquamarine = new Color("0x7FFFD4");
  public static final Color Azure = new Color("0xF0FFFF");
  public static final Color Beige = new Color("0xF5F5DC");
  public static final Color Bisque = new Color("0xFFE4C4");
  public static final Color Black = new Color("0x000000");
  public static final Color BlanchedAlmond = new Color("0xFFEBCD");
  public static final Color Blue = new Color("0x0000FF");
  public static final Color BlueViolet = new Color("0x8A2BE2");
  public static final Color Brown = new Color("0xA52A2A");
  public static final Color BurlyWood = new Color("0xDEB887");
  public static final Color CadetBlue = new Color("0x5F9EA0");
  public static final Color Chartreuse = new Color("0x7FFF00");
  public static final Color Chocolate = new Color("0xD2691E");
  public static final Color Coral = new Color("0xFF7F50");
  public static final Color CornflowerBlue = new Color("0x6495ED");
  public static final Color Cornsilk = new Color("0xFFF8DC");
  public static final Color Crimson = new Color("0xDC143C");
  public static final Color Cyan = new Color("0x00FFFF");
  public static final Color DarkBlue = new Color("0x00008B");
  public static final Color DarkCyan = new Color("0x008B8B");
  public static final Color DarkGoldenRod = new Color("0xB8860B");
  public static final Color DarkGray = new Color("0xA9A9A9");
  public static final Color DarkGreen = new Color("0x006400");
  public static final Color DarkKhaki = new Color("0xBDB76B");
  public static final Color DarkMagenta = new Color("0x8B008B");
  public static final Color DarkOliveGreen = new Color("0x556B2F");
  public static final Color Darkorange = new Color("0xFF8C00");
  public static final Color DarkOrchid = new Color("0x9932CC");
  public static final Color DarkRed = new Color("0x8B0000");
  public static final Color DarkSalmon = new Color("0xE9967A");
  public static final Color DarkSeaGreen = new Color("0x8FBC8F");
  public static final Color DarkSlateBlue = new Color("0x483D8B");
  public static final Color DarkSlateGray = new Color("0x2F4F4F");
  public static final Color DarkTurquoise = new Color("0x00CED1");
  public static final Color DarkViolet = new Color("0x9400D3");
  public static final Color DeepPink = new Color("0xFF1493");
  public static final Color DeepSkyBlue = new Color("0x00BFFF");
  public static final Color DimGray = new Color("0x696969");
  public static final Color DodgerBlue = new Color("0x1E90FF");
  public static final Color FireBrick = new Color("0xB22222");
  public static final Color FloralWhite = new Color("0xFFFAF0");
  public static final Color ForestGreen = new Color("0x228B22");
  public static final Color Fuchsia = new Color("0xFF00FF");
  public static final Color Gainsboro = new Color("0xDCDCDC");
  public static final Color GhostWhite = new Color("0xF8F8FF");
  public static final Color Gold = new Color("0xFFD700");
  public static final Color GoldenRod = new Color("0xDAA520");
  public static final Color Gray = new Color("0x808080");
  public static final Color Green = new Color("0x008000");
  public static final Color GreenYellow = new Color("0xADFF2F");
  public static final Color HoneyDew = new Color("0xF0FFF0");
  public static final Color HotPink = new Color("0xFF69B4");
  public static final Color IndianRed = new Color("0xCD5C5C");
  public static final Color Indigo = new Color("0x4B0082");
  public static final Color Ivory = new Color("0xFFFFF0");
  public static final Color Khaki = new Color("0xF0E68C");
  public static final Color Lavender = new Color("0xE6E6FA");
  public static final Color LavenderBlush = new Color("0xFFF0F5");
  public static final Color LawnGreen = new Color("0x7CFC00");
  public static final Color LemonChiffon = new Color("0xFFFACD");
  public static final Color LightBlue = new Color("0xADD8E6");
  public static final Color LightCoral = new Color("0xF08080");
  public static final Color LightCyan = new Color("0xE0FFFF");
  public static final Color LightGoldenRodYellow = new Color("0xFAFAD2");
  public static final Color LightGreen = new Color("0x90EE90");
  public static final Color LightGrey = new Color("0xD3D3D3");
  public static final Color LightPink = new Color("0xFFB6C1");
  public static final Color LightSalmon = new Color("0xFFA07A");
  public static final Color LightSeaGreen = new Color("0x20B2AA");
  public static final Color LightSkyBlue = new Color("0x87CEFA");
  public static final Color LightSlateGray = new Color("0x778899");
  public static final Color LightSteelBlue = new Color("0xB0C4DE");
  public static final Color LightYellow = new Color("0xFFFFE0");
  public static final Color Lime = new Color("0x00FF00");
  public static final Color LimeGreen = new Color("0x32CD32");
  public static final Color Linen = new Color("0xFAF0E6");
  public static final Color Magenta = new Color("0xFF00FF");
  public static final Color Maroon = new Color("0x800000");
  public static final Color MediumAquaMarine = new Color("0x66CDAA");
  public static final Color MediumBlue = new Color("0x0000CD");
  public static final Color MediumOrchid = new Color("0xBA55D3");
  public static final Color MediumPurple = new Color("0x9370D8");
  public static final Color MediumSeaGreen = new Color("0x3CB371");
  public static final Color MediumSlateBlue = new Color("0x7B68EE");
  public static final Color MediumSpringGreen = new Color("0x00FA9A");
  public static final Color MediumTurquoise = new Color("0x48D1CC");
  public static final Color MediumVioletRed = new Color("0xC71585");
  public static final Color MidnightBlue = new Color("0x191970");
  public static final Color MintCream = new Color("0xF5FFFA");
  public static final Color MistyRose = new Color("0xFFE4E1");
  public static final Color Moccasin = new Color("0xFFE4B5");
  public static final Color NavajoWhite = new Color("0xFFDEAD");
  public static final Color Navy = new Color("0x000080");
  public static final Color OldLace = new Color("0xFDF5E6");
  public static final Color Olive = new Color("0x808000");
  public static final Color OliveDrab = new Color("0x6B8E23");
  public static final Color Orange = new Color("0xFFA500");
  public static final Color OrangeRed = new Color("0xFF4500");
  public static final Color Orchid = new Color("0xDA70D6");
  public static final Color PaleGoldenRod = new Color("0xEEE8AA");
  public static final Color PaleGreen = new Color("0x98FB98");
  public static final Color PaleTurquoise = new Color("0xAFEEEE");
  public static final Color PaleVioletRed = new Color("0xD87093");
  public static final Color PapayaWhip = new Color("0xFFEFD5");
  public static final Color PeachPuff = new Color("0xFFDAB9");
  public static final Color Peru = new Color("0xCD853F");
  public static final Color Pink = new Color("0xFFC0CB");
  public static final Color Plum = new Color("0xDDA0DD");
  public static final Color PowderBlue = new Color("0xB0E0E6");
  public static final Color Purple = new Color("0x800080");
  public static final Color Red = new Color("0xFF0000");
  public static final Color RosyBrown = new Color("0xBC8F8F");
  public static final Color RoyalBlue = new Color("0x4169E1");
  public static final Color SaddleBrown = new Color("0x8B4513");
  public static final Color Salmon = new Color("0xFA8072");
  public static final Color SandyBrown = new Color("0xF4A460");
  public static final Color SeaGreen = new Color("0x2E8B57");
  public static final Color SeaShell = new Color("0xFFF5EE");
  public static final Color Sienna = new Color("0xA0522D");
  public static final Color Silver = new Color("0xC0C0C0");
  public static final Color SkyBlue = new Color("0x87CEEB");
  public static final Color SlateBlue = new Color("0x6A5ACD");
  public static final Color SlateGray = new Color("0x708090");
  public static final Color Snow = new Color("0xFFFAFA");
  public static final Color SpringGreen = new Color("0x00FF7F");
  public static final Color SteelBlue = new Color("0x4682B4");
  public static final Color Tan = new Color("0xD2B48C");
  public static final Color Teal = new Color("0x008080");
  public static final Color Thistle = new Color("0xD8BFD8");
  public static final Color Tomato = new Color("0xFF6347");
  public static final Color Turquoise = new Color("0x40E0D0");
  public static final Color Violet = new Color("0xEE82EE");
  public static final Color Wheat = new Color("0xF5DEB3");
  public static final Color White = new Color("0xFFFFFF");
  public static final Color WhiteSmoke = new Color("0xF5F5F5");
  public static final Color Yellow = new Color("0xFFFF00");
  public static final Color YellowGreen = new Color("0x9ACD32");

  /** @param color color in RRGGBB syntax, like in html/css, but without the leading # character */
  public Color(String color) {
    this.color = color;
  }

  /**
   * @param color color in #RRGGBB syntax, like in html/css, but without the leading # character
   * @param alpha Alpha channel, Values from 00 up to FF, 00 means not transparent, FF means fully
   *     transparent
   */
  public Color(String color, String alpha) {
    this.color = color;
    this.alpha = alpha;
  }

  /** @return the color */
  public String getColor() {
    return color;
  }

  /**
   * @param color the color to set
   * @return this instance
   */
  public Color setColor(String color) {
    this.color = color;
    return this;
  }

  /** @return the alpha */
  public String getAlpha() {
    return alpha;
  }

  /**
   * @param alpha the alpha to set
   * @return this instance
   */
  public Color setAlpha(String alpha) {
    this.alpha = alpha;
    return this;
  }
  
  /** 
   * @return color in command line format 
   * */
  @Override
  public String toString() {
    return getFfmpegColor();
  }
  
  /**
   * Deprecated in favor of toString
   * @return The specified color in command line format.
   */
  public String getFfmpegColor() {
    return "0x" + color + alpha;
  }
}
