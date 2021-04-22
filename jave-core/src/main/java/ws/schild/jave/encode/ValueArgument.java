package ws.schild.jave.encode;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A ValueArgument is an EncodingArgument that is optionally present based on the presence of the
 * provided valueGetter.
 *
 * @author mressler
 */
public class ValueArgument implements EncodingArgument {

  private final ArgType argumentType;
  private final String argumentName;
  private final Function<EncodingAttributes, Optional<String>> valueGetter;

  public ValueArgument(
      ArgType argType,
      String argumentName,
      Function<EncodingAttributes, Optional<String>> valueGetter) {
    this.argumentType = argType;
    this.argumentName = argumentName;
    this.valueGetter = valueGetter;
  }

  protected Boolean isPresent(EncodingAttributes context) {
    return getValue(context).isPresent();
  }

  @Override
  public Stream<String> getArguments(EncodingAttributes context) {
    return getValue(context).map(value -> Stream.of(getName(), value)).orElseGet(Stream::empty);
  }

  private String getName() {
    return argumentName;
  }

  private Optional<String> getValue(EncodingAttributes context) {
    return valueGetter.apply(context);
  }

  @Override
  public ArgType getArgType() {
    return argumentType;
  }
}
