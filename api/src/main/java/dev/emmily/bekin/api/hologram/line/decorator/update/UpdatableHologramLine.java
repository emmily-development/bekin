package dev.emmily.bekin.api.hologram.line.decorator.update;

import dev.emmily.bekin.api.event.line.HologramLineUpdateEvent;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.AbstractLineDecorator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.Objects;

public class UpdatableHologramLine
  extends AbstractLineDecorator
  implements HologramLine {
  public static UpdatableHologramLine decorate(HologramLine wrappedLine,
                                               long updatePeriod) {
    return new UpdatableHologramLine(wrappedLine, updatePeriod);
  }

  private final long updatePeriod;
  private long lastUpdate;

  @ConstructorProperties({"wrappedLine", "updatePeriod"})
  public UpdatableHologramLine(HologramLine wrappedLine,
                               long updatePeriod) {
    super(wrappedLine);
    this.updatePeriod = updatePeriod;
  }

  public boolean canUpdate() {
    return System.currentTimeMillis() - lastUpdate >= updatePeriod;
  }

  public void update() {
    if (!canUpdate()) {
      return;
    }

    lastUpdate = System.currentTimeMillis();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UpdatableHologramLine)) return false;
    UpdatableHologramLine that = (UpdatableHologramLine) o;
    return updatePeriod == that.updatePeriod && lastUpdate == that.lastUpdate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(updatePeriod, lastUpdate);
  }
}
