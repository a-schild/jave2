package ws.schild.jave.encode.enums;


public enum PresetEnum {

    /**
     * Code quality from low to high
     */
    ULTRAFAST("ultrafast"),
    SUPERFAST("superfast"),
    VERYFAST("veryfast"),
    FASTER("faster"),
    FAST("fast"),
    MEDIUM("medium"),
    SLOW("slow"),
    SLOWER("slower"),
    VERYSLOW("veryslow"),
    PLACEBO("placebo");

    private final String presetName;

    private PresetEnum(String presetName) {
        this.presetName = presetName;
    }

    public String getPresetName() {
        return presetName;
    }
}
