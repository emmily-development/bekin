package dev.emmily.bekin.api.event;

import dev.emmily.bekin.api.hologram.Hologram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Represents an event related to holograms
 * and players.
 */
public abstract class HologramEvent
  extends Event {
  private final Hologram hologram;
  private final Player player;

  public HologramEvent(Hologram hologram, Player player) {
    this.hologram = hologram;
    this.player = player;
  }

  public Hologram getHologram() {
    return hologram;
  }

  public Player getPlayer() {
    return player;
  }
}
