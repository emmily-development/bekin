package dev.emmily.bekin.api.util.lang;

import dev.emmily.bekin.api.util.lang.locale.LocaleLanguageProvider;
import org.bukkit.entity.Player;

public interface LanguageProvider {
  LanguageProvider LOCALE = new LocaleLanguageProvider();

  static LanguageProvider locale() {
    return LOCALE;
  }

  String getLanguage(Player player);
}
