package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.update.UpdatableHologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static dev.emmily.bekin.api.hologram.line.HologramLine.line;

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
  private final HologramHandler hologramHandler;

  @Inject
  public HologramCommand(ModelRepository<PaginatedMessage> paginatedMessageRepository,
                         Linguist<Player> playerLinguist,
                         HologramHandler hologramHandler) {
    this.paginatedMessageRepository = paginatedMessageRepository;
    this.playerLinguist = playerLinguist;
    this.hologramHandler = hologramHandler;
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

  @Command(
    names = "test-clickable"
  )
  public void runTestClickableCommand(@Sender Player player) {
    Hologram hologram = Hologram
      .builder()
      .id(UUID.randomUUID().toString())
      .renderAuthorizer(RenderAuthorizer.ofPermission("sexo"))
      .renderDistance(8)
      .addLines(
        line(TextProvider.staticText("Hello, " + player.getName())),
        ClickableHologramLine.decorate(
          UpdatableHologramLine.decorate(line(TextProvider.staticText("Ping")), TimeUnit.SECONDS.toMillis(3)),
          HologramClickAction.sendMessage("Pong")
        )
      )
      .build();

    hologramHandler.spawn(hologram);
    hologramHandler.renderForEveryone(hologram);
  }
}
