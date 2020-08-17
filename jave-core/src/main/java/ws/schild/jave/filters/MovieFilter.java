package ws.schild.jave.filters;

import java.io.File;

public class MovieFilter extends Filter {

  public MovieFilter(File source) {
    super("movie");
    addOrderedArgument(source.getAbsolutePath());
  }

  public MovieFilter(File source, String outputLabel) {
    this(source);
    addOutputLabel(outputLabel);
  }
}
