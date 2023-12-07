package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.plugin.message.MessageMode;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HologramAddLineCommand
  implements CommandClass {
  private final HologramHandler hologramHandler;
  private final MessageHandler messageHandler;

  @Inject
  public HologramAddLineCommand(HologramHandler hologramHandler,
                                MessageHandler messageHandler) {
    this.hologramHandler = hologramHandler;
    this.messageHandler = messageHandler;
  }

  @Command(
    names = "add-line"
  )
  public void runAddLineCommand(@Sender Player player,
                                @Named("hologram") Hologram hologram,
                                @Named("content") @Text String content) {
    hologramHandler.destroy(hologram);

    hologram.addLine(HologramLine.line(TextProvider.staticText(content)));

    hologramHandler.spawn(hologram);

    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      hologramHandler.render(hologram, onlinePlayer);
    }

    messageHandler.sendReplacingIn(
      player, MessageMode.SUCCESS,
      "hologram.added-line",
      "%hologram%", hologram.getId(),
      "%line%", content
    );
  }

  @Command(
    names = "set-line"
  )
  public void runSetLineCommand(@Sender Player player,
                                @Named("hologram") Hologram hologram,
                                @Named("index") int index,
                                @Named("content") @Text String content) {
    if (hologram.getLines().get(index) == null) {
      messageHandler.sendReplacingIn(
        player, MessageMode.ERROR,
        "hologram.line-not-found",
        "%hologram%", hologram.getId(),
        "%line%", index
      );

      return;
    }

    hologramHandler.destroy(hologram);

    hologram.replaceLine(index, HologramLine.line(TextProvider.staticText(content)));

    hologramHandler.spawn(hologram);

    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      hologramHandler.render(hologram, onlinePlayer);
    }

    messageHandler.sendReplacingIn(
      player, MessageMode.SUCCESS,
      "hologram.replaced-line",
      "%hologram%", hologram.getId(),
      "%index%", index,
      "%line%", content
    );
  }
}
