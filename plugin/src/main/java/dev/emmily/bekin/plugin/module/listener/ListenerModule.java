package dev.emmily.bekin.plugin.module.listener;

import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.core.listener.HologramClickListener;
import dev.emmily.bekin.plugin.listener.PlayerJoinListener;
import dev.emmily.bekin.plugin.listener.PlayerQuitListener;
import me.yushust.inject.AbstractModule;
import org.bukkit.event.Listener;

public class ListenerModule
  extends AbstractModule {
  private final HologramRegistry hologramRegistry;

  public ListenerModule(HologramRegistry hologramRegistry) {
    this.hologramRegistry = hologramRegistry;
  }

  @Override
  protected void configure() {
    multibind(Listener.class)
      .asSet()
      .to(PlayerJoinListener.class)
      .to(PlayerQuitListener.class)
      .toInstance(new HologramClickListener(hologramRegistry));
  }
}
