package dev.emmily.bekin.api.event.line;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class HologramLineUpdateEvent
  extends HologramLineEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public HologramLineUpdateEvent(HologramLine line,
                                 Player player) {
    super(line, player);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
