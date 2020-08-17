package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.List;

import ws.schild.jave.filtergraphs.TrimAndWatermark.TrimInfo;
import ws.schild.jave.filters.FadeFilter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.helpers.FadeDirection;

public class TrimFadeAndWatermark extends FilterAndWatermark {
  
  public TrimFadeAndWatermark(File watermark, List<TrimInfo> trimInfo) {
    super(watermark, trimInfo.size());
    
    init((i, isLast) -> filterchainForTrimInfo(i, isLast, trimInfo.get(i), 0.1));
  }

  public static FilterChain filterchainForTrimInfo(Integer i, boolean isLast, TrimInfo info, Double fadeDuration) {
    FilterChain toReturn = TrimAndWatermark.filterchainForTrimInfo(info);
    
    if (i != 0) {
      toReturn.prependFilter(new FadeFilter(FadeDirection.IN, 0.0, fadeDuration));
    }
    
    if (!isLast) {
      toReturn.addFilter(new FadeFilter(FadeDirection.OUT, info.trimDuration - fadeDuration, fadeDuration));
    }
    
    return toReturn;
  }
  
}
