package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ws.schild.jave.filters.ConcatFilter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MovieFilter;
import ws.schild.jave.filters.OverlayFilter;
import ws.schild.jave.filters.helpers.FilterChainProvider;
import ws.schild.jave.filters.helpers.OverlayLocation;

/**
 * An abstract filtergraph that will run a filter on multiple input videos then concatenate and
 * watermark the result.
 *
 * <p>Implementors are expected to provide the filter chains to this abstract class via the init()
 * method.
 */
public abstract class FilterAndWatermark extends FilterGraph {

  private File watermark;
  private Integer inputVideoCount;

  public FilterAndWatermark(File watermark, Integer inputVideoCount) {
    super();

    this.watermark = watermark;
    this.inputVideoCount = inputVideoCount;
  }

  /**
   * MUST be called prior to getExpression. It is intended that the subclassing function calls this
   * in the constructor.
   */
  protected void init(FilterChainProvider provider) {
    // Apply the provided filterchain for each input video
    IntStream.range(0, inputVideoCount)
        .mapToObj(i -> filterchainForInputIndex(i, inputVideoCount, provider))
        .forEach(this::addChain);

    // Concatenate all input videos
    addChain(
        new FilterChain(
            new ConcatFilter(
                    IntStream.range(0, inputVideoCount)
                        .mapToObj(this::labelForOutput)
                        .collect(Collectors.toList()))
                .addOutputLabel("concatenated")));

    // Finally overlay the watermark
    addChain(
        new FilterChain(
            new MovieFilter(watermark), // Movie output is the second input to the overlay filter
            new OverlayFilter("concatenated", OverlayLocation.BOTTOM_RIGHT, -10, -10)));
  }

  private FilterChain filterchainForInputIndex(
      Integer i, Integer count, FilterChainProvider provider) {
    return provider
        .provide(i, i == count - 1)
        .setInputLabel(i.toString())
        .setOutputLabel(labelForOutput(i));
  }

  private String labelForOutput(Integer i) {
    return "filtered" + i;
  }
}
