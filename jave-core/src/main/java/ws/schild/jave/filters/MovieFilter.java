package ws.schild.jave.filters;

import java.io.File;

public class MovieFilter extends Filter {

  /**
   * A simple instantiation of the <a href="https://ffmpeg.org/ffmpeg-filters.html#movie-1">movie</a> filter.
   * @param source The source image to be used for this movie filter.
   */
  public MovieFilter(File source) {
    super("movie");
    /*
     * Need escaping special characters []\':,;
     */
    addOrderedArgument(escapingPath(source.getAbsolutePath()));
  }

  public MovieFilter(File source, String outputLabel) {
    this(source);
    addOutputLabel(outputLabel);
  }

}
