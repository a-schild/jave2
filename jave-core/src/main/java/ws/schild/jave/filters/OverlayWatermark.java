package ws.schild.jave.filters;

import java.io.File;
import java.util.Optional;

public class OverlayWatermark implements VideoFilter {
	
	private File watermark;
	private OverlayLocation location;
	private Integer offsetX;
	private Integer offsetY;
	
	/**
	 * Create an overlay filter that will overlay a watermark image on the video.
	 * @param watermark The location of the watermark image
	 * @param location The location on the video that the watermark should be overlaid
	 * @param offsetX The offset from the location that the watermark should be offset. Positive values move the image right. Negative values move it left.
	 * @param offsetY The offset from the location that the watermark should be offset. Positive values move the image down. Negative values move it up.
	 */
	public OverlayWatermark(File watermark, OverlayLocation location, Integer offsetX, Integer offsetY) {
		this.watermark = watermark;
		this.location = location;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	@Override
	public String getExpression() {
		return "movie=" + watermark.getAbsolutePath() + " [watermark]; [0:v][watermark] overlay=" + 
				location.getExpression(Optional.ofNullable(offsetX), Optional.ofNullable(offsetY));
    }
	
}
