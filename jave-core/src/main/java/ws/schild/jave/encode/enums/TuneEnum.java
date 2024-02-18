package ws.schild.jave.encode.enums;

public enum TuneEnum {
    /**
     * use for high quality movie content; lowers deblocking
     */
    FILM("film"),
    /**
     * good for cartoons; uses higher deblocking and more reference frames
     */
    ANIMATION("animation"),
    /**
     * preserves the grain structure in old, grainy film material
     */
    GRAIN("grain"),
    /**
     * good for slideshow-like content
     */
    STILLIMAGE("stillimage"),
    /**
     * allows faster decoding by disabling certain filters
     */
    FASTDECODE("fastdecode"),
    /**
     * good for fast encoding and low-latency streaming
     */
    ZEROLATENCY("zerolatency"),
    /**
     * ignore this as it is only used for codec development
     */
    PSNR("psnr"),
    /**
     * ignore this as it is only used for codec development
     */
    SSIM("ssim");

    private final String tuneName;

    TuneEnum(String tuneName) {
        this.tuneName = tuneName;
    }

    public String getTuneName() {
        return tuneName;
    }
}
