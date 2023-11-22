package dev.emmily.bekin.api.hologram.line.provider;

import dev.emmily.bekin.api.hologram.line.provider.fixed.StaticTextProvider;
import dev.emmily.bekin.api.hologram.line.provider.i18n.MultiLanguageTextProvider;

/**
 * Utility that provides common text functions for hologram lines. This is
 * used for deserialization, since lambdas cnanot be directly (de)serialized.
 */
public interface TextProviders {
  static TextProvider staticText(String text) {
    return new StaticTextProvider(text);
  }

  static TextProvider multiLanguage(String path) {
    return new MultiLanguageTextProvider(path);
  }
}
