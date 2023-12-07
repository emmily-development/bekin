package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.bekin.plugin.message.MessageMode;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HologramCreateCommand
  implements CommandClass {
  private final HologramHandler hologramHandler;
  private final HologramRegistry hologramRegistry;
  private final MessageHandler messageHandler;

  @Inject
  public HologramCreateCommand(HologramHandler hologramHandler,
                               HologramRegistry hologramRegistry,
                               MessageHandler messageHandler) {
    this.hologramHandler = hologramHandler;
    this.hologramRegistry = hologramRegistry;
    this.messageHandler = messageHandler;
  }

  @Command(
    names = "create"
  )
  public void runCreateCommand(@Sender Player player,
                               @Named("id") String id,
                               @Named("renderDistance") @OptArg("8") int renderDistance) {
    if (hologramRegistry.get(id) != null) {
      messageHandler.sendReplacingIn(
        player, MessageMode.ERROR,
        "hologram.already-exists",
        "%hologram%", id
      );

      return;
    }

    Hologram hologram = Hologram
      .builder()
      .id(id)
      .position(Vector3D.fromBukkit(player.getLocation()))
      .renderDistance(renderDistance)
      .addLine(HologramLine.line(TextProvider.staticText("Bekin")))
      .build();

    hologramRegistry.register(hologram);
    hologramHandler.spawn(hologram);

    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      hologramHandler.render(hologram, onlinePlayer);
    }

    messageHandler.sendReplacingIn(
      player, MessageMode.SUCCESS,
      "hologram.created",
      "%hologram%", id
    );
  }
}
