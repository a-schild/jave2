package ws.schild.jave.filters.helpers;

import ws.schild.jave.filters.FilterChain;

@FunctionalInterface
public interface FilterChainProvider {

  public abstract FilterChain provide(Integer i, boolean isLast);
  
}
