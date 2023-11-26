package dev.emmily.bekin.api.util.lang.locale;

import dev.emmily.bekin.api.util.lang.LanguageProvider;
import org.bukkit.entity.Player;

public class LocaleLanguageProvider
  implements LanguageProvider {
  @Override
  public String getLanguage(Player player) {
    return player.spigot().getLocale().split("_")[0];
  }
}
