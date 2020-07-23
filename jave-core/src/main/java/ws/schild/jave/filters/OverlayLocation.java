package ws.schild.jave.filters;

import java.util.Optional;

public enum OverlayLocation {
	
	TOP_LEFT("0", "0"),
	TOP_RIGHT("main_w-overlay_w", "0"),
	BOTTOM_RIGHT("main_w-overlay_w", "main_h-overlay_h"),
	BOTTOM_LEFT("0", "main_h-overlay_h");
	
	private String x;
	private String y;
	
	private OverlayLocation(String x, String y) {
		this.x = x;
		this.y = y;
	}
	
	public String getExpression(Optional<Integer> offsetX, Optional<Integer> offsetY) {
		return x + offsetX.map(Object::toString).orElse("") + ":" + y + offsetY.map(Object::toString).orElse("");
	}
	
}
