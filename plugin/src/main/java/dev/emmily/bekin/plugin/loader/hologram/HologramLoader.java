package dev.emmily.bekin.plugin.loader.hologram;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.error.ErrorNotifier;
import dev.emmily.bekin.plugin.loader.Loader;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HologramLoader
  implements Loader {
  private final HologramRegistry hologramRegistry;
  private final BekinPlugin plugin;
  private final ErrorNotifier errorNotifier;

  @Inject
  public HologramLoader(HologramRegistry hologramRegistry,
                        BekinPlugin plugin,
                        ErrorNotifier errorNotifier) {
    this.hologramRegistry = hologramRegistry;
    this.plugin = plugin;
    this.errorNotifier = errorNotifier;
  }

  @Override
  public void onLoad() {
    plugin.getLogger().info("Loading holograms...");

    hologramRegistry
      .findAll()
      .whenComplete((holograms, error) -> {
        if (error != null) {
          plugin.getLogger().warning("An error has occurred while trying to retrieve saved holograms");
          errorNotifier.notify(error);

          return;
        }

        for (Hologram hologram : holograms) {
          hologramRegistry.register(hologram);
        }

        plugin.getLogger().info("Successfully loaded " + holograms.size() + " holograms");
      });
  }

  @Override
  public void onUnload() {
    plugin.getLogger().info("Saving holograms...");

    CompletableFuture.runAsync(() -> {
      List<Hologram> holograms = hologramRegistry.getAll();

      for (Hologram hologram : holograms) {
        hologramRegistry.save(hologram).join();
      }

      plugin.getLogger().info("Successfully saved " + holograms.size() + " holograms");
    });
  }
}
