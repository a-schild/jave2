package ws.schild.jave.encode;

import java.util.stream.Stream;

public interface EncodingArgument {

	public Stream<String> getArguments(EncodingAttributes context);

	public ArgType getArgType();

}