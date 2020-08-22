package ws.schild.jave.encode.enums;

public enum X264_PROFILE {
  BASELINE("baseline"),
  MAIN("main"),
  HIGH("high"),
  HIGH10("high10"),
  HIGH422("high422"),
  HIGH444("high444");
  
  private final String modeName;

  private X264_PROFILE(String modeName) {
    this.modeName = modeName;
  }

  public String getModeName() {
    return modeName;
  }
};