package dev.emmily.bekin.api.hologram.line.provider.i18n;

import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;

public class MultiLanguageTextProvider
  implements TextProvider {
  private final String path;

  public MultiLanguageTextProvider(String path) {
    this.path = path;
  }

  @Override
  public String apply(Player player,
                      MessageHandler messageHandler) {
    return messageHandler.get(player, path);
  }
}
