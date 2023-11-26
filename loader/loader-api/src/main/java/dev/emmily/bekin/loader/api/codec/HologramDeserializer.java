package dev.emmily.bekin.loader.api.codec;

import dev.emmily.bekin.api.hologram.Hologram;

public interface HologramDeserializer {
  Hologram deserialize(String source);
}
