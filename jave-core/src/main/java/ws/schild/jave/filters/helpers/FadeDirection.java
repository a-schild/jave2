package ws.schild.jave.filters.helpers;

public enum FadeDirection {
  IN("in"),
  OUT("out");
  
  private final String friendlyName;
  
  private FadeDirection(String friendlyName) {
    this.friendlyName = friendlyName;
  }
  
  @Override
  public String toString() {
    return friendlyName;
  }
}
