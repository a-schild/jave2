package ws.schild.jave.encode;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class PredicateArgument implements EncodingArgument {

	private ArgType argumentType;
	private Stream<String> arguments;
	private Predicate<EncodingAttributes> predicate;
	
	public PredicateArgument(ArgType argType, String argument, Predicate<EncodingAttributes> predicate) {
		this.argumentType = argType;
		this.arguments = Stream.of(argument);
		this.predicate = predicate;
	}
	
	public PredicateArgument(ArgType argType, String argument1, String argument2, Predicate<EncodingAttributes> predicate) {
		this.argumentType = argType;
		this.arguments = Stream.of(argument1, argument2);
		this.predicate = predicate;
	}
	
	@Override
	public Stream<String> getArguments(EncodingAttributes context) {
		if (predicate.test(context)) {
			return arguments;
		} else {
			return Stream.empty();
		}
	}

	@Override
	public ArgType getArgType() {
		return argumentType;
	}

}
