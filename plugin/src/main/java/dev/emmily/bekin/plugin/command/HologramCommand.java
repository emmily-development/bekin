package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.plugin.message.PaginatedMessage;
import dev.emmily.sigma.api.repository.ModelRepository;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.MessageUtils;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.language.Linguist;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
  names = {
    "bekin", "hologram", "holo"
  }
)
@SubCommandClasses({
  HologramAddLineCommand.class, HologramCreateCommand.class,
  HologramDeleteCommand.class, HologramMoveCommand.class,
  HologramRemoveLineCommand.class
})
public class HologramCommand
  implements CommandClass {
  private final ModelRepository<PaginatedMessage> paginatedMessageRepository;
  private final Linguist<Player> playerLinguist;

  @Inject
  public HologramCommand(ModelRepository<PaginatedMessage> paginatedMessageRepository,
                         Linguist<Player> playerLinguist) {
    this.paginatedMessageRepository = paginatedMessageRepository;
    this.playerLinguist = playerLinguist;
  }

  @Command(
    names = ""
  )
  public void runInfoCommand(@Sender Player player) {
    PaginatedMessage message = paginatedMessageRepository.find(playerLinguist.getLanguage(player));

    if (message == null) {
      message = paginatedMessageRepository.find("en");
    }

    send(player, message);
  }

  @Command(
    names = "previous"
  )
  public void runPreviousCommand(@Sender Player player) {
    PaginatedMessage message = paginatedMessageRepository.find(playerLinguist.getLanguage(player));

    if (message == null) {
      message = paginatedMessageRepository.find("en");
    }

    message.previousPage(player);

    send(player, message);
  }

  @Command(
    names = "next"
  )
  public void runNextCommand(@Sender Player player) {
    PaginatedMessage message = paginatedMessageRepository.find(playerLinguist.getLanguage(player));

    if (message == null) {
      message = paginatedMessageRepository.find("en");
    }

    message.nextPage(player);

    send(player, message);
  }

  private void send(Player player,
                    PaginatedMessage message) {
    for (String line : message.getCurrentPage(player)) {
      player.sendMessage(line);
    }

    MessageUtils.sendMessage(player, MessageUtils.kyoriToBungee(message.getNavigation().getFooter()));
  }
}
