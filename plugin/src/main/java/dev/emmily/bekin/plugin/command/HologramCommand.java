package dev.emmily.bekin.plugin.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.update.UpdatableHologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import dev.emmily.bekin.api.util.lang.LanguageProvider;
import dev.emmily.bekin.plugin.message.PaginatedMessage;
import dev.emmily.sigma.api.repository.ModelRepository;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.MessageUtils;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Random;
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
  private static final Random RANDOM = new Random();

  private final ModelRepository<PaginatedMessage> paginatedMessageRepository;
  private final HologramHandler hologramHandler;
  private final HologramRegistry hologramRegistry;

  @Inject
  public HologramCommand(ModelRepository<PaginatedMessage> paginatedMessageRepository,
                         HologramHandler hologramHandler,
                         HologramRegistry hologramRegistry) {
    this.paginatedMessageRepository = paginatedMessageRepository;
    this.hologramHandler = hologramHandler;
    this.hologramRegistry = hologramRegistry;
  }

  @Command(
    names = ""
  )
  public void runInfoCommand(@Sender Player player) {
    PaginatedMessage message = paginatedMessageRepository.find(LanguageProvider.locale().getLanguage(player));

    if (message == null) {
      message = paginatedMessageRepository.find("en");
    }

    send(player, message);
  }

  @Command(
    names = "previous"
  )
  public void runPreviousCommand(@Sender Player player) {
    PaginatedMessage message = paginatedMessageRepository.find(LanguageProvider.locale().getLanguage(player));

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
    PaginatedMessage message = paginatedMessageRepository.find(LanguageProvider.locale().getLanguage(player));

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
  public void runTestClickableCommand(@Sender Player sender) {
    Hologram hologram = Hologram
      .builder()
      .id(UUID.randomUUID().toString())
      .position(Position.fromBukkit(sender.getLocation()))
      .renderAuthorizer(RenderAuthorizer.ofPermission("sexo"))
      .renderDistance(8)
      .addLines(
        line(TextProvider.staticText("Hello, " + sender.getName())),
        ClickableHologramLine.decorate(
          UpdatableHologramLine.decorate(line(player -> "Random: " + RANDOM.nextInt()), TimeUnit.SECONDS.toMillis(3)),
          HologramClickAction.sendMessage("Pong")
        )/*,
        ClickableHologramLine.decorate(
          HologramLine.line(TextProvider.staticText("Click me")),
          HologramClickAction.sendMessage("Test")
        )
        */
      )
      .build();

    hologramRegistry.register(hologram);
    hologramHandler.spawn(hologram);
    hologramHandler.renderForEveryone(hologram);
  }
}
