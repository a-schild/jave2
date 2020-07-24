package ws.schild.jave.filters;

import java.util.Optional;

public class OverlayFilter extends Filter {

	public OverlayFilter(
		String baseInputLabel, 
		String overlayInputLabel, 
		OverlayLocation location, 
		Integer offsetX, 
		Integer offsetY) 
	{
		super("overlay");
		addInputLabel(baseInputLabel, overlayInputLabel);
		addOrderedArgument(
			location.getX(Optional.ofNullable(offsetX)),
			location.getY(Optional.ofNullable(offsetY)));
	}

}
