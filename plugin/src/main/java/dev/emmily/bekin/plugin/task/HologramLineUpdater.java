package dev.emmily.bekin.plugin.task;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.update.UpdatableHologramLine;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;

import javax.inject.Inject;
import java.util.List;

public class HologramLineUpdater implements Runnable {
  private final HologramRegistry hologramRegistry;
  private final HologramHandler hologramHandler;

  @Inject
  public HologramLineUpdater(HologramRegistry hologramRegistry,
                             HologramHandler hologramHandler) {
    this.hologramRegistry = hologramRegistry;
    this.hologramHandler = hologramHandler;
  }

  @Override
  public void run() {
    List<Hologram> holograms = hologramRegistry.getAll();

    for (Hologram hologram : holograms) {
      List<HologramLine> lines = hologram.getLines();

      for (HologramLine line : lines) {
        if (line instanceof UpdatableHologramLine) {
          UpdatableHologramLine updatableLine = (UpdatableHologramLine) line;

          if (!updatableLine.canUpdate()) {
            continue;
          }


        } else if (line instanceof ClickableHologramLine) {
          ClickableHologramLine clickableLine = (ClickableHologramLine) line;

          if (clickableLine.getWrappedLine() instanceof UpdatableHologramLine) {
            UpdatableHologramLine updatableLine = (UpdatableHologramLine) clickableLine.getWrappedLine();
            updatableLine.update();
          }
        }
      }
    }
  }
}
