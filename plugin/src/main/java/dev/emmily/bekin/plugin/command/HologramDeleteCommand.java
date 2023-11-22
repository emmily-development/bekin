package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.plugin.error.ErrorNotifier;
import dev.emmily.bekin.plugin.message.MessageMode;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class HologramDeleteCommand
  implements CommandClass {
  private final HologramHandler hologramHandler;
  private final HologramRegistry hologramRegistry;
  private final ErrorNotifier errorNotifier;
  private final MessageHandler messageHandler;
  private final Set<String> queuedPlayers;

  @Inject
  public HologramDeleteCommand(HologramHandler hologramHandler,
                               HologramRegistry hologramRegistry,
                               ErrorNotifier errorNotifier,
                               MessageHandler messageHandler) {
    this.hologramHandler = hologramHandler;
    this.hologramRegistry = hologramRegistry;
    this.errorNotifier = errorNotifier;
    this.messageHandler = messageHandler;
    this.queuedPlayers = new HashSet<>();
  }

  @Command(
    names = {"delete", "remove"}
  )
  public void runDeleteCommand(@Sender Player player,
                               @Named("hologram") Hologram hologram) {
    String id = player.getUniqueId().toString();

    if (!queuedPlayers.contains(id)) {
      messageHandler.sendIn(
        player, MessageMode.INFO,
        "hologram.confirm-deletion"
      );

      queuedPlayers.add(id);

      return;
    }

    queuedPlayers.remove(id);
    hologramHandler.destroy(hologram);
    hologramRegistry.unregister(hologram);
    hologramRegistry
      .delete(hologram)
      .whenComplete(($, error) -> {
        if (error != null) {
          errorNotifier.notify(error);

          messageHandler.sendReplacingIn(
            player, MessageMode.ERROR,
            "hologram.error-deleting",
            "%hologram%", hologram.getId()
          );
        } else {
          messageHandler.sendReplacingIn(
            player, MessageMode.SUCCESS,
            "hologram.deleted",
            "%hologram%", hologram.getId()
          );
        }
      });
  }
}
