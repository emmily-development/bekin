package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.bekin.plugin.message.MessageMode;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HologramMoveCommand
  implements CommandClass {
  private final HologramHandler hologramHandler;
  private final MessageHandler messageHandler;

  @Inject
  public HologramMoveCommand(HologramHandler hologramHandler,
                             MessageHandler messageHandler) {
    this.hologramHandler = hologramHandler;
    this.messageHandler = messageHandler;
  }

  @Command(
    names = {"move", "tp"}
  )
  public void runMoveCommand(@Sender Player player,
                             @Named("hologram") Hologram hologram) {
    Location bukkit = player.getLocation();
    Vector3D newPosition = Vector3D.fromBukkit(bukkit);

    hologramHandler.move(hologram, newPosition);

    messageHandler.sendReplacingIn(
      player, MessageMode.SUCCESS,
      "hologram.moved",
      "%hologram%", hologram.getId(),
      "%new-position%", newPosition.toString()
    );
  }
}
