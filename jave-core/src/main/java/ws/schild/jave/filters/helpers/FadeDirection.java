package ws.schild.jave.filters.helpers;

public enum FadeDirection {
  IN("in"),
  OUT("out");
  
  private String friendlyName;
  
  private FadeDirection(String friendlyName) {
    this.friendlyName = friendlyName;
  }
  
  public String toString() {
    return friendlyName;
  }
}
