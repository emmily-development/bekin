package dev.emmily.bekin.loader.api.hologram.line;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.loader.api.hologram.line.provider.TextProviderTemplate;

import java.util.List;

/**
 * Represents a {@link HologramLine} within a configuration file.
 */
public interface HologramLineTemplate {
  List<Type> getType();

  TextProviderTemplate getTextProvider();

  enum Type {
    SIMPLE,
    CLICKABLE,
    UPDATABLE
  }
}
