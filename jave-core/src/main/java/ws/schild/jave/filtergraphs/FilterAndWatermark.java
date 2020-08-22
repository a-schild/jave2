package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ws.schild.jave.filters.ConcatFilter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MovieFilter;
import ws.schild.jave.filters.OverlayFilter;
import ws.schild.jave.filters.helpers.OverlayLocation;

/**
 * An abstract filtergraph that will run a filter on multiple input videos then concatenate and
 * watermark the result.
 *
 * <p>Implementors are expected to provide the filter chains to this abstract class via the init()
 * method.
 */
public abstract class FilterAndWatermark extends FilterGraph {

  public FilterAndWatermark(File watermark, List<FilterChain> inputFilterChains) {
    super();

    // Apply the provided filterchain for each input video
    IntStream.range(0, inputFilterChains.size())
      .mapToObj(i -> prepFilterChain(inputFilterChains.get(i), i))
      .forEach(this::addChain);
    
    // Concatenate all input videos
    addChain(
        new FilterChain(
            new ConcatFilter(
                    IntStream.range(0, inputFilterChains.size())
                        .mapToObj(this::labelForOutput)
                        .collect(Collectors.toList()))
                .addOutputLabel("concatenated")));

    // Finally overlay the watermark
    addChain(
        new FilterChain(
            new MovieFilter(watermark), // Movie output is the second input to the overlay filter
            new OverlayFilter("concatenated", OverlayLocation.BOTTOM_RIGHT, -10, -10)));
  }

  private FilterChain prepFilterChain(FilterChain chain, Integer i) {
    return chain
        .setInputLabel(i.toString())
        .setOutputLabel(labelForOutput(i));
  }

  private String labelForOutput(Integer i) {
    return "filtered" + i;
  }
}
