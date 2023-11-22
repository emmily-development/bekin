package dev.emmily.bekin.api.hologram.line.decorator.update;

import dev.emmily.bekin.api.event.line.HologramLineUpdateEvent;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.AbstractHologramLineDecorator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.Collection;

public class UpdatableHologramLineDecorator
  extends AbstractHologramLineDecorator
  implements HologramLine {
  public static UpdatableHologramLineDecorator decorate(HologramLine wrappedLine,
                                                        long updatePeriod) {
    return new UpdatableHologramLineDecorator(wrappedLine, updatePeriod);
  }

  private final long updatePeriod;
  private long lastUpdate;

  @ConstructorProperties({"wrappedLine", "updatePeriod"})
  public UpdatableHologramLineDecorator(HologramLine wrappedLine,
                                        long updatePeriod) {
    super(wrappedLine);
    this.updatePeriod = updatePeriod;
  }

  public boolean canUpdate() {
    return System.currentTimeMillis() - lastUpdate >= updatePeriod;
  }

  public void update(Collection<Player> players) {
    if (!canUpdate()) {
      return;
    }

    for (Player player : players) {
      Bukkit.getPluginManager().callEvent(new HologramLineUpdateEvent(
        this, player
      ));
    }

    lastUpdate = System.currentTimeMillis();
  }
}
