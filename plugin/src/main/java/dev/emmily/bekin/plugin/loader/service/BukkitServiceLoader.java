package dev.emmily.bekin.plugin.loader.service;

import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.loader.Loader;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import javax.inject.Inject;

public class BukkitServiceLoader
  implements Loader {
  private final BekinPlugin plugin;
  private final ServicesManager servicesManager;
  private final HologramHandler hologramHandler;
  private final HologramRegistry hologramRegistry;

  @Inject
  public BukkitServiceLoader(BekinPlugin plugin,
                             HologramHandler hologramHandler,
                             HologramRegistry hologramRegistry) {
    this.plugin = plugin;
    this.servicesManager = plugin.getServer().getServicesManager();
    this.hologramHandler = hologramHandler;
    this.hologramRegistry = hologramRegistry;
  }

  @Override
  public void onLoad() {
    plugin.getLogger().info("Registering services...");

    servicesManager.register(
      HologramHandler.class,
      hologramHandler,
      plugin,
      ServicePriority.Normal
    );
    servicesManager.register(
      HologramRegistry.class,
      hologramRegistry,
      plugin,
      ServicePriority.Normal
    );

    plugin.getLogger().info("Successfully registered 2 services");
  }

  @Override
  public void onUnload() {
    plugin.getLogger().info("Unregistering services...");

    servicesManager.unregisterAll(plugin);

    plugin.getLogger().info("Successfully unregistered 2 services");
  }
}
