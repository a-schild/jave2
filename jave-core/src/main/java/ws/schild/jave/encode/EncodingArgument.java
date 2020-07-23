package ws.schild.jave.encode;

import java.util.stream.Stream;

/**
 * An EncodingArgument is a placeholder for a future argument to FFMPEG. It uses the
 * EncodingAttributes object to determine context and provides a Stream<String>
 * of arguments back to the caller to bbe used as arguments.
 * @author mressler
 *
 */
public interface EncodingArgument {

	public Stream<String> getArguments(EncodingAttributes context);

	public ArgType getArgType();

}