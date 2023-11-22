package dev.emmily.bekin.plugin.listener;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class PlayerJoinListener
  implements Listener {
  private final HologramHandler hologramHandler;
  private final HologramRegistry hologramRegistry;

  @Inject
  public PlayerJoinListener(HologramHandler hologramHandler,
                            HologramRegistry hologramRegistry) {
    this.hologramHandler = hologramHandler;
    this.hologramRegistry = hologramRegistry;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    for (Hologram hologram : hologramRegistry.getAll()) {
      hologramHandler.render(hologram, player);
    }
  }
}
