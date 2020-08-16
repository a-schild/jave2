package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.SetPtsFilter;
import ws.schild.jave.filters.TrimFilter;

/**
 * Trim and watermark any number of input videos.
 *
 */
public class TrimAndWatermark extends FilterAndWatermark {

  public TrimAndWatermark(File watermark, List<TrimInfo> trimInfo) {
    super(watermark, trimInfo.size());

    init((i, isLast) -> filterchainForTrimInfo(trimInfo.get(i)));
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

  public static FilterChain filterchainForTrimInfo(TrimInfo info) {
    return new FilterChain(
        new TrimFilter(info.trimStart, info.trimDuration),
        new SetPtsFilter());
  }
}
