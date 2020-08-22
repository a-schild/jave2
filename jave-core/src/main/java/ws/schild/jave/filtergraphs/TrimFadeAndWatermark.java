package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ws.schild.jave.filtergraphs.TrimAndWatermark.TrimInfo;
import ws.schild.jave.filters.FadeFilter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.helpers.FadeDirection;

public class TrimFadeAndWatermark extends FilterAndWatermark {
  
  public TrimFadeAndWatermark(File watermark, List<TrimInfo> trimInfo) {
    super(
        watermark, 
        IntStream.range(0, trimInfo.size())
            .mapToObj(i -> filterChainForTrimInfo(trimInfo.get(i), fadesFromIndex(i, trimInfo.size()), 0.1))
            .collect(Collectors.toList()));
  }

  public static EnumSet<FadeDirection> fadesFromIndex(Integer i, Integer size) {
    EnumSet<FadeDirection> toFade = EnumSet.noneOf(FadeDirection.class);
    
    if (i != 0) {
      toFade.add(FadeDirection.IN);
    }
    
    if (i < size) {
      toFade.add(FadeDirection.OUT);
    }
    
    return toFade;
  }
  
  public static FilterChain filterChainForTrimInfo(TrimInfo info, EnumSet<FadeDirection> fades, Double fadeDuration) {
    FilterChain toReturn = TrimAndWatermark.filterChainForTrimInfo(info);
    
    if (fades.contains(FadeDirection.IN)) {
      toReturn.prependFilter(new FadeFilter(FadeDirection.IN, 0.0, fadeDuration));
    }
    
    if (fades.contains(FadeDirection.OUT)) {
      toReturn.addFilter(new FadeFilter(FadeDirection.OUT, info.trimDuration - fadeDuration, fadeDuration));
    }
    
    return toReturn;
  }
  
}
