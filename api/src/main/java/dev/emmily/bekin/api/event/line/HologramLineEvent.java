package dev.emmily.bekin.api.event.line;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Represents an event related to holograms
 * lines and players.
 */
public abstract class HologramLineEvent
  extends Event {
  private final HologramLine line;
  private final Player player;

  public HologramLineEvent(HologramLine line, Player player) {
    this.line = line;
    this.player = player;
  }

  public HologramLine getLine() {
    return line;
  }

  public Player getPlayer() {
    return player;
  }
}
