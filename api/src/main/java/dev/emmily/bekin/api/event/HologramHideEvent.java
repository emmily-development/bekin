package dev.emmily.bekin.api.event;

import dev.emmily.bekin.api.hologram.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class HologramHideEvent
  extends HologramEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public HologramHideEvent(Hologram hologram,
                           Player player) {
    super(hologram, player);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
