package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.plugin.message.MessageMode;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HologramRemoveLineCommand
  implements CommandClass {
  private final HologramHandler hologramHandler;
  private final MessageHandler messageHandler;

  @Inject
  public HologramRemoveLineCommand(HologramHandler hologramHandler,
                                   MessageHandler messageHandler) {
    this.hologramHandler = hologramHandler;
    this.messageHandler = messageHandler;
  }

  @Command(
    names = {"remove-line", "delete-line"}
  )
  public void runRemoveLineCommand(@Sender Player player,
                                   @Named("hologram") Hologram hologram,
                                   @Named("index") int index) {
    if (hologram.getLines().get(index) == null) {
      messageHandler.sendReplacingIn(
        player, MessageMode.ERROR,
        "hologram.line-not-found",
        "%hologram%", hologram.getId(),
        "%index%", index
      );

      return;
    }

    hologram.removeLine(index);

    hologramHandler.destroy(hologram);
    hologramHandler.spawn(hologram);

    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      hologramHandler.render(hologram, onlinePlayer);
    }

    messageHandler.sendReplacingIn(
      player, MessageMode.SUCCESS,
      "hologram.deleted-line",
      "%hologram%", hologram.getId(),
      "%index%", index
    );
  }
}
