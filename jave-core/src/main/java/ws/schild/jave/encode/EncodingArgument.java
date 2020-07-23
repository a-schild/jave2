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

	/**
	 * Gets the Stream of arguments given the EncodingAttributes as context. Implementers must take
	 * care to return a new Stream on each successive call as doing otherwise will result in the stream
	 * already being operated on exceptions.
	 * @param context
	 * @return
	 */
	public Stream<String> getArguments(EncodingAttributes context);

	public ArgType getArgType();

}