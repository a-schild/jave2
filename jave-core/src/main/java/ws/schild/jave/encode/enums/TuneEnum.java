package ws.schild.jave.encode.enums;

public enum TuneEnum {
    FILM("film"),//use for high quality movie content; lowers deblocking
    ANIMATION("animation"),//good for cartoons; uses higher deblocking and more reference frames
    GRAIN("grain"),//preserves the grain structure in old, grainy film material
    STILLIMAGE("stillimage"),//good for slideshow-like content
    FASTDECODE("fastdecode"),//allows faster decoding by disabling certain filters
    ZEROLATENCY("zerolatency"),//good for fast encoding and low-latency streaming
    PSNR("psnr"),//ignore this as it is only used for codec development
    SSIM("ssim");//ignore this as it is only used for codec development

    private final String tuneName;

    TuneEnum(String tuneName) {
        this.tuneName = tuneName;
    }

    public String getTuneName() {
        return tuneName;
    }
}
