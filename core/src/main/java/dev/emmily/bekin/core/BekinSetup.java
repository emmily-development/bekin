package dev.emmily.bekin.core;

import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.core.listener.HologramClickListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public interface BekinSetup {
  static void injectListeners(Plugin plugin,
                              HologramRegistry registry) {
    PluginManager pluginManager = plugin.getServer().getPluginManager();
    pluginManager.registerEvents(new HologramClickListener(registry), plugin);
  }
}
