package dev.emmily.bekin.core;

import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.core.listener.HologramClickListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public interface BekinSetup {
  static void setup(JavaPlugin plugin) {
    PluginManager pluginManager = plugin.getServer().getPluginManager();
    HologramRegistry hologramRegistry = plugin
      .getServer()
      .getServicesManager()
      .getRegistration(HologramRegistry.class)
      .getProvider();

    if (hologramRegistry == null) {
      plugin.getLogger().severe("Bekin has not been loaded yet.");
      pluginManager.disablePlugin(plugin);

      return;
    }


    pluginManager.registerEvents(new HologramClickListener(hologramRegistry), plugin);
  }
}
