package dev.emmily.bekin.api.hologram.line.decorator.click;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.AbstractLineDecorator;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import dev.emmily.bekin.api.util.lang.LanguageProvider;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;

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
  private final Map<String, BoundingBox> boundingBoxes;

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
    this.boundingBoxes = new HashMap<>();
  }

  /**
   * Returns the map of bounding boxes per language. Each player may
   * see the line differently, depending on the language.
   * Therefore, a unique bounding box is associated with each player.
   *
   * @return The map of bounding boxes (language to bounding box mapping).
   */
  public Map<String, BoundingBox> getBoundingBoxes() {
    return boundingBoxes;
  }

  public void registerBoundingBox(Player player,
                                  BoundingBox boundingBox) {
    boundingBoxes.computeIfAbsent(LanguageProvider.locale().getLanguage(player), k -> boundingBox);
  }

  public BoundingBox getBoundingBox(Player player) {
    return boundingBoxes.get(LanguageProvider.locale().getLanguage(player));
  }

  public BoundingBox getBoundingBox(String language) {
    return boundingBoxes.get(language);
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
}