package dev.emmily.bekin.plugin.loader.listener;

import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.loader.Loader;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;
import java.util.Set;

public class ListenerLoader
  implements Loader {
  private final BekinPlugin plugin;
  private final Set<Listener> listeners;

  @Inject
  public ListenerLoader(BekinPlugin plugin,
                        Set<Listener> listeners) {
    this.plugin = plugin;
    this.listeners = listeners;
  }

  @Override
  public void onLoad() {
    plugin.getLogger().info("Loading listeners...");

    PluginManager pluginManager = plugin.getServer().getPluginManager();

    for (Listener listener : listeners) {
      pluginManager.registerEvents(listener, plugin);
    }

    plugin.getLogger().info("Successfully loaded" + listeners.size() + " listeners");
  }

  @Override
  public void onUnload() {

  }
}
