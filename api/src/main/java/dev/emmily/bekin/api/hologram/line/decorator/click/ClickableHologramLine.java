package dev.emmily.bekin.api.hologram.line.decorator.click;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.AbstractLineDecorator;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Represents a hologram line with a click feature.
 * <p>
 * This decorator allows hologram lines to be clickable by players.
 * When a player clicks on a line decorated with this feature,
 * a specified action is performed.
 * To achieve this, custom bounding boxes are used, positioned
 * around the armor stand's custom name instead of the armor
 * stand itself. This approach is used because the hit-box of an
 * armor stand occupies 2 blocks vertically, and clickable lines may
 * be placed one below the other, causing collision issues without
 * custom bounding boxes.
 */
public class ClickableHologramLine
  extends AbstractLineDecorator
  implements HologramLine {
  public static ClickableHologramLine decorate(HologramLine wrappedLine,
                                               HologramClickAction clickAction) {
    return new ClickableHologramLine(wrappedLine, clickAction);
  }

  private final HologramClickAction clickAction;

  /**
   * Decorates the given {@code wrappedLine} with the click feature.
   *
   * @param wrappedLine The hologram line to be wrapped and made clickable.
   * @param clickAction The action to be performed when a player clicks this line.
   */
  @ConstructorProperties({"wrappedLine", "clickAction"})
  public ClickableHologramLine(HologramLine wrappedLine,
                               HologramClickAction clickAction) {
    super(wrappedLine);
    this.clickAction = clickAction;
  }

  /**
   * Performs the click action when a player interacts with this
   * clickable line.
   *
   * @param player The player who interacted with the line.
   */
  public void onClick(Player player) {
    clickAction.accept(player);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ClickableHologramLine)) return false;
    ClickableHologramLine that = (ClickableHologramLine) o;
    return Objects.equals(clickAction, that.clickAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clickAction);
  }
}