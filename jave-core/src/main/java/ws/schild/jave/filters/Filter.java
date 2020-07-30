package ws.schild.jave.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A filter as described by <a href="https://ffmpeg.org/ffmpeg-filters.html#Filtergraph-syntax-1">
 * FFMPEG Documentation</a>.
 *
 * <p>A filter has an ordered list of input labels, a name, arguments, and an ordered list of output
 * labels. Arguments can be either ordered or named. If both are present, ordered must be emitted
 * first.
 *
 * <p>Currently <a href="https://ffmpeg.org/ffmpeg-filters.html#Notes-on-filtergraph-escaping">
 * filtergraph escaping</a> is not the responsibility of this class. All arguments must be
 * pre-escaped by the time they get to this class.
 *
 * <p>It is intended that this class is not used directly. Instead, implementers will add a specific
 * implementation of the filter they are implementing. A complete list can be found in <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#Filtergraph-syntax-1">FFMPEG Documentation</a>.
 *
 * @author mressler
 */
public abstract class Filter implements VideoFilter {

  private List<String> inputLinkLabels;
  private String name;
  private List<String> orderedArguments;
  private Map<String, String> namedArguments;
  private List<String> outputLinkLabels;

  /**
   * Create a filter with the specified name with no input/output labels or arguments.
   *
   * @param name The name of the filter.
   */
  public Filter(String name) {
    inputLinkLabels = new ArrayList<>();
    this.name = name;
    orderedArguments = new ArrayList<>();
    namedArguments = new HashMap<>();
    outputLinkLabels = new ArrayList<>();
  }

  /**
   * Add an input label to the list of input labels for this filter
   *
   * @param label The name of the input label(s)
   * @return this Filter for builder pattern magic
   */
  public Filter addInputLabel(String... label) {
    inputLinkLabels.addAll(Arrays.asList(label));
    return this;
  }

  /**
   * Add an ordered argument to the list of arguments for this filter
   *
   * @param arg Any number of ordered arguments
   * @return this Filter for builder pattern magic
   */
  public Filter addOrderedArgument(String... arg) {
    orderedArguments.addAll(Arrays.asList(arg));
    return this;
  }

  /**
   * Add a named argument to the set of named arguments for this filter
   *
   * @param name The name of the argument
   * @param value The value for the argument
   * @return this Filter for builder pattern magic
   */
  public Filter addNamedArgument(String name, String value) {
    namedArguments.put(name, value);
    return this;
  }

  /**
   * Add an output label to the list of output labels for this filter
   *
   * @param label The name of the input label
   * @return this Filter for builder pattern magic
   */
  public Filter addOutputLabel(String... label) {
    outputLinkLabels.addAll(Arrays.asList(label));
    return this;
  }

  @Override
  public String getExpression() {
    return formatLinkLabels(inputLinkLabels)
        + name
        + formatArguments()
        + formatLinkLabels(outputLinkLabels);
  }

  private static String formatLinkLabels(List<String> labels) {
    return labels.stream().map(labelName -> "[" + labelName + "]").collect(Collectors.joining());
  }

  private String formatArguments() {
    return ((orderedArguments.size() + namedArguments.size() > 0) ? "='" : "")
        + Stream.concat(
                orderedArguments.stream(),
                namedArguments.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()))
            .collect(Collectors.joining(":"))
        + ((orderedArguments.size() + namedArguments.size() > 0) ? "'" : "");
  }
}
