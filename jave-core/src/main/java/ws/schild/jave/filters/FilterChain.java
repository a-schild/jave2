package ws.schild.jave.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A filterchain as described by <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#Filtergraph-syntax-1">FFMPEG Documentation</a>.
 *
 * <p>A filterchain is a comma separated series of filters.
 *
 * @author mressler
 */
public class FilterChain implements VideoFilter {

  private final List<Filter> filters;

  /** Create an empty filterchain. */
  public FilterChain() {
    filters = new ArrayList<>();
  }
  /**
   * Create a filterchain with the specified filters
   *
   * @param filters The ordered list of filters in this chain
   */
  public FilterChain(Filter... filters) {
    this.filters = new ArrayList<>(Arrays.asList(filters));
  }

  /**
   * Add one Filter to this filterchain
   *
   * @param filter The Filter to add to this chain.
   * @return this FilterChain for builder pattern magic
   */
  public FilterChain addFilter(Filter filter) {
    filters.add(filter);
    return this;
  }
  
  public FilterChain prependFilter(Filter filter) {
    filters.add(0, filter);
    return this;
  }
  
  /**
   * Adds an input label to the first filter in this chain.
   * @param label The label to use for the input label for the first filter in this chain
   * @return this FilterChain for builder pattern magic
   * @throws IndexOutOfBoundsException if there are no filters in this chain.
   */
  public FilterChain setInputLabel(String label) {
    filters.get(0).addInputLabel(label);
    return this;
  }
  
  /**
   * Adds an output label to the first filter in this chain.
   * @param label The label to use for the output label for the last filter in this chain
   * @return this FilterChain for builder pattern magic
   * @throws IndexOutOfBoundsException if there are no filters in this chain.
   */
  public FilterChain setOutputLabel(String label) {
    filters.get(filters.size() - 1).addOutputLabel(label);
    return this;
  }

  @Override
  public String getExpression() {
    return filters.stream().map(VideoFilter::getExpression).collect(Collectors.joining(","));
  }
}
