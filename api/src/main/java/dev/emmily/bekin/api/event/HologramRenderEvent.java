package dev.emmily.bekin.api.event;

import dev.emmily.bekin.api.hologram.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when an hologram is rendered
 * to a given player.
 */
public class HologramRenderEvent
  extends HologramEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public HologramRenderEvent(Hologram hologram,
                             Player player) {
    super(hologram, player);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
