package ws.schild.jave.filters.helpers;

import java.util.Optional;

/**
 * Use this class to specify the starting location of your overlay. This accounts for video and
 * overlay dimensions and still allows for relative offset.
 *
 * @author mressler
 */
public enum OverlayLocation {
  TOP_LEFT(null, null),
  TOP_RIGHT("main_w-overlay_w", null),
  BOTTOM_RIGHT("main_w-overlay_w", "main_h-overlay_h"),
  BOTTOM_LEFT(null, "main_h-overlay_h");

  private final Optional<String> x;
  private final Optional<String> y;

  private OverlayLocation(String x, String y) {
    this.x = Optional.ofNullable(x);
    this.y = Optional.ofNullable(y);
  }

  public String getExpression(Optional<Integer> offsetX, Optional<Integer> offsetY) {
    return getX(offsetX) + ":" + getY(offsetY);
  }

  private static String resolveExpression(Optional<String> location, Optional<Integer> offset) {
    Optional<String> offsetValue = offset.map(Object::toString);
    return location.map(loc -> loc.concat(offsetValue.orElse(""))).orElse(offsetValue.orElse("0"));
  }

  public String getX(Optional<Integer> offset) {
    return resolveExpression(x, offset);
  }

  public String getY(Optional<Integer> offset) {
    return resolveExpression(y, offset);
  }
}
