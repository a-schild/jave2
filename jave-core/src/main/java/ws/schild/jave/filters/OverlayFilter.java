package ws.schild.jave.filters;

import java.util.Optional;

import ws.schild.jave.filters.helpers.OverlayLocation;

/**
 * An implementation of the overlay filter as specified by <a
 * href="https://ffmpeg.org/ffmpeg-filters.html#overlay-1">FFMPEG Documentation</a>
 *
 * @author mressler
 */
public class OverlayFilter extends Filter {

  public OverlayFilter() {
    super("overlay");
  }
  
  /**
   * Overlay video onto {@code baseInputLabel} at {@code location}. Offsets specify x/y 
   * offsets from the four locations. This constructor has an implicit unmatched input pad that 
   * needs to be filled by a previous filter in the chain.
   * 
   * @param baseInputLabel The location to overlay video onto.
   * @param location One of the four corners.
   * @param offsetX An offset from one of the four corners.
   * @param offsetY An offset from one of the four corners.
   */
  public OverlayFilter(
      String baseInputLabel, 
      OverlayLocation location, 
      Integer offsetX, 
      Integer offsetY) 
  {
    super("overlay");
    addInputLabel(baseInputLabel);
    addOrderedArgument(
        location.getX(Optional.ofNullable(offsetX)), location.getY(Optional.ofNullable(offsetY)));
  }

  public OverlayFilter(
      String baseInputLabel,
      String overlayInputLabel,
      OverlayLocation location,
      Integer offsetX,
      Integer offsetY) 
  {
    this(baseInputLabel, location, offsetX, offsetY);
    addInputLabel(overlayInputLabel);
  }
}
