package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.List;

import ws.schild.jave.filtergraphs.TrimAndWatermark.TrimInfo;
import ws.schild.jave.filters.FadeFilter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.helpers.FadeDirection;

public class TrimFadeAndWatermark extends FilterAndWatermark {
  
  private Double fadeDuration;
  
  public TrimFadeAndWatermark(File watermark, List<TrimInfo> trimInfo) {
    super(watermark, trimInfo.size());

    fadeDuration = 0.1;
    
    init((i, isLast) -> filterchainForTrimInfo(i, isLast, trimInfo.get(i)));
  }

  protected FilterChain filterchainForTrimInfo(Integer i, boolean isLast, TrimInfo info) {
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
