package ws.schild.jave.filters;

import java.io.File;

public class OverlayWatermark extends FilterGraph {
	
	/**
	 * Create an overlay filter that will overlay a watermark image on the video.
	 * @param watermark The location of the watermark image
	 * @param location The location on the video that the watermark should be overlaid
	 * @param offsetX The offset from the location that the watermark should be offset. Positive values move the image right. Negative values move it left.
	 * @param offsetY The offset from the location that the watermark should be offset. Positive values move the image down. Negative values move it up.
	 */
	public OverlayWatermark(File watermark, OverlayLocation location, Integer offsetX, Integer offsetY) {
		super(
			new FilterChain(new MovieFilter(watermark, "watermark")),
			new FilterChain(new OverlayFilter("0:v", "watermark", location, offsetX, offsetY))
		);
	}
	
}
