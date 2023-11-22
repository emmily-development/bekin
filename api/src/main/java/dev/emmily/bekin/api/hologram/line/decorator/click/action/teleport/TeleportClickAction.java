package dev.emmily.bekin.api.hologram.line.decorator.click.action.teleport;

import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public class TeleportClickAction
  implements HologramClickAction {
  private final Vector3D position;

  @ConstructorProperties("position")
  public TeleportClickAction(Vector3D position) {
    this.position = position;
  }

  @Override
  public void accept(Player player) {
    player.teleport(position.toBukkit().toLocation(Bukkit.getWorld(position.getWorld())));
  }
}
