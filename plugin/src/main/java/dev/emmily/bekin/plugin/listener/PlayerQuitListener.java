package dev.emmily.bekin.plugin.listener;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

public class PlayerQuitListener
  implements Listener {
  private final HologramHandler hologramHandler;
  private final HologramRegistry hologramRegistry;

  @Inject
  public PlayerQuitListener(HologramHandler hologramHandler,
                            HologramRegistry hologramRegistry) {
    this.hologramHandler = hologramHandler;
    this.hologramRegistry = hologramRegistry;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    for (Hologram hologram : hologramRegistry.getAll()) {
      if (hologram.canView(player)) {
        hologramHandler.hide(hologram, player);
      }
    }
  }
}
