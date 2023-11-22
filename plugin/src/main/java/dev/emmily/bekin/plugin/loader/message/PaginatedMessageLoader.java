package dev.emmily.bekin.plugin.loader.message;

import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.loader.Loader;
import dev.emmily.bekin.plugin.message.PaginatedMessage;
import dev.emmily.sigma.api.repository.ModelRepository;
import me.yushust.message.MessageHandler;
import me.yushust.message.source.MessageSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class PaginatedMessageLoader
  implements Loader {
  private static final Pattern LANG_FILE_PATTERN = Pattern.compile("^lang_[A-Za-z]{2}\\.yml$");

  private final MessageSource messageSource;
  private final ModelRepository<PaginatedMessage> paginatedMessageRepository;
  private final BekinPlugin plugin;

  @Inject
  public PaginatedMessageLoader(MessageHandler messageHandler,
                                ModelRepository<PaginatedMessage> paginatedMessageRepository,
                                BekinPlugin plugin) {
    this.messageSource = messageHandler.getSource();
    this.paginatedMessageRepository = paginatedMessageRepository;
    this.plugin = plugin;
  }

  @Override
  public void onLoad() {
    plugin.getLogger().info("Loading info messages...");

    File langFolder = new File(plugin.getDataFolder(), "lang");
    File[] langFiles = langFolder.listFiles(pathname -> LANG_FILE_PATTERN.matcher(pathname.getName()).matches());

    if (langFiles == null) {
      throw new IllegalArgumentException("No lang files found");
    }

    for (File langFile : langFiles) {
      String lang = langFile
        .getName()
        .replace("lang_", "")
        .replace(".yml", "");

      List<String> message = (List<String>) messageSource.get(lang, "hologram.info.content");

      PaginatedMessage.Navigation navigation = new PaginatedMessage.Navigation(
        Component
          .text((String) messageSource.get(lang, "hologram.info.navigation.previous"))
          .clickEvent(ClickEvent.runCommand("hologram previous")),
        Component
          .text((String) messageSource.get(lang, "hologram.info.navigation.next"))
          .clickEvent(ClickEvent.runCommand("hologram next")),
        (String) messageSource.get(lang, "hologram.info.navigation.footer")
      );

      PaginatedMessage infoMessage = new PaginatedMessage(
        lang,
        message,
        7,
        navigation
      );

      paginatedMessageRepository.create(infoMessage);
    }

    plugin.getLogger().info("Successfully loaded info messages for " + (langFiles.length + 1) + " languages");
  }

  @Override
  public void onUnload() {
    paginatedMessageRepository.deleteManyByQuery((Predicate<PaginatedMessage>) paginatedMessage -> true);
  }
}
