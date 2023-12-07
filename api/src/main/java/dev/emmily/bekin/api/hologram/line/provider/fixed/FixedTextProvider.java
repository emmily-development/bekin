package dev.emmily.bekin.api.hologram.line.provider.fixed;

import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public class FixedTextProvider
  implements TextProvider {
  private final String text;

  @ConstructorProperties("text")
  public FixedTextProvider(String text) {
    this.text = text;
  }

  @Override
  public String apply(Player player) {
    return text;
  }
}
