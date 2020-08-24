package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.SetPtsFilter;
import ws.schild.jave.filters.TrimFilter;

/** Trim and watermark any number of input videos. */
public class TrimAndWatermark extends FilterAndWatermark {

  public TrimAndWatermark(File watermark, List<TrimInfo> trimInfo) {
    super(
        watermark,
        IntStream.range(0, trimInfo.size())
            .mapToObj(i -> filterChainForTrimInfo(trimInfo.get(i)))
            .collect(Collectors.toList()));
  }

  public TrimAndWatermark(File watermark, Double trimStart, Double trimDuration) {
    this(watermark, Arrays.asList(new TrimInfo(trimStart, trimDuration)));
  }

  public static class TrimInfo {
    public Double trimStart;
    public Double trimDuration;

    public TrimInfo(Double trimStart, Double trimDuration) {
      this.trimStart = trimStart;
      this.trimDuration = trimDuration;
    }
  }

  public static FilterChain filterChainForTrimInfo(TrimInfo info) {
    return new FilterChain(new TrimFilter(info.trimStart, info.trimDuration), new SetPtsFilter());
  }
}
