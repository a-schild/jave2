package ws.schild.jave.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A filtergraph as described by <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#Filtergraph-syntax-1">FFMPEG Documentation</a>.
 *
 * <p>A filtergraph can optionally start with sws_flags for scaling of outputs and is then composed
 * of a semi-colon separated series of filterchains.
 *
 * @author mressler
 */
public class FilterGraph implements VideoFilter {

  private Optional<String> swsFlags;
  private List<FilterChain> chains;

  /** Create an empty filtergraph. */
  public FilterGraph() {
    swsFlags = Optional.empty();
    chains = new ArrayList<>();
  }
  /**
   * Create a filtergraph with a specified list of filterchains.
   *
   * @param chains The list of filterchains to be used in this filtergraph.
   */
  public FilterGraph(FilterChain... chains) {
    this();
    this.chains = new ArrayList<>(Arrays.asList(chains));
  }
  /**
   * Create a filtergraph with a specified list of filterchains and specified sws_flags.
   *
   * @param chains The list of filterchains to be used in this filtergraph.
   * @param swsFlags The sws_flags parameter to pass to libavfilter scale filters.
   */
  public FilterGraph(String swsFlags, FilterChain... chains) {
    this(chains);
    this.swsFlags = Optional.of(swsFlags);
  }

  /**
   * Add a filterchain to this filtergraph.
   *
   * @param chain The filterchain to add to this filtergraph.
   * @return this FilterGraph for builder pattern magic
   */
  public FilterGraph addChain(FilterChain chain) {
    chains.add(chain);
    return this;
  }

  /**
   * set the sws_flags to pass to libavfilter scale filters.
   *
   * @param swsFlags The flags that will; be passed to libavfilter scale filters.
   * @return this FilterGraph for builder pattern magic
   */
  public FilterGraph setSwsFlags(String swsFlags) {
    this.swsFlags = Optional.of(swsFlags);
    return this;
  }

  @Override
  public String getExpression() {
    return swsFlags.map(s -> "sws_flags=" + s + ";").orElse("")
        + chains.stream().map(VideoFilter::getExpression).collect(Collectors.joining(";"));
  }
}
