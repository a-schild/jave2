package ws.schild.jave.filters;

import java.io.File;

public class MovieFilter extends Filter {

	public MovieFilter(File source, String outputLabel) {
		super("movie");
		addOrderedArgument(source.getAbsolutePath());
		addOutputLabel(outputLabel);
	}

}
