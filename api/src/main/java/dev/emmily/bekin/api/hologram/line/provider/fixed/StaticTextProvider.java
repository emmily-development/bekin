package dev.emmily.bekin.api.hologram.line.provider.fixed;

import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public class StaticTextProvider
  implements TextProvider {
  private final String text;

  @ConstructorProperties("text")
  public StaticTextProvider(String text) {
    this.text = text;
  }

  @Override
  public String apply(Player player,
                      MessageHandler messageHandler) {
    return text;
  }
}
