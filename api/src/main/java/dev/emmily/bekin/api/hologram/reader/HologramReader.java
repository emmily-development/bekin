package dev.emmily.bekin.api.hologram.reader;

import dev.emmily.bekin.api.hologram.Hologram;

import java.io.File;
import java.util.List;

public interface HologramReader {
  Hologram read(File file);

  List<Hologram> bulkRead(File folder);
}
