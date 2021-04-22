package ws.schild.jave.encode;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A PredicateArgument is an EncodingArgument that adds its arguments based on the provided
 * predicate
 *
 * @author mressler
 */
public class PredicateArgument implements EncodingArgument {

  private final ArgType argumentType;
  private final Supplier<Stream<String>> arguments;
  private final Predicate<EncodingAttributes> predicate;

  public PredicateArgument(
      ArgType argType, String argument, Predicate<EncodingAttributes> predicate) {
    this.argumentType = argType;
    this.arguments = () -> Stream.of(argument);
    this.predicate = predicate;
  }

  public PredicateArgument(
      ArgType argType,
      String argument1,
      String argument2,
      Predicate<EncodingAttributes> predicate) {
    this.argumentType = argType;
    this.arguments = () -> Stream.of(argument1, argument2);
    this.predicate = predicate;
  }

  @Override
  public Stream<String> getArguments(EncodingAttributes context) {
    if (predicate.test(context)) {
      return arguments.get();
    } else {
      return Stream.empty();
    }
  }

  @Override
  public ArgType getArgType() {
    return argumentType;
  }
}
