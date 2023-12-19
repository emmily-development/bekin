package dev.emmily.bekin.api.hologram.line.decorator.click.action.teleport;

import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public class TeleportClickAction
  implements HologramClickAction {
  private final Position position;

  @ConstructorProperties("position")
  public TeleportClickAction(Position position) {
    this.position = position;
  }

  @Override
  public void accept(Player player) {
    player.teleport(position.toBukkit().toLocation(Bukkit.getWorld(position.getWorld())));
  }
}
