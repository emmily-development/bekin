package dev.emmily.bekin.loader.api;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.loader.api.codec.HologramDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Represents an abstract hologram loader. Its implementations are intended to handle the
 * deserialization of holograms from a single, specific file type (e.g. JSON). Every implementation
 * of this interface must follow the hologram loading specification: (soon).
 */
public interface HologramLoader {
  /**
   * Returns the unique identifier of this hologram loader platform.
   *
   * @return The unique identifier of this hologram loader platform.
   */
  String getPlatform();

  /**
   * Returns the file type (extension) associated with this hologram loader.
   *
   * @return The file type (extension) associated with this hologram loader.
   */
  String getFileType();

  HologramDeserializer getDeserializer();

  /**
   * Loads a hologram from the specified {@code file}.
   *
   * @param file The file to load the hologram from.
   * @return The loaded hologram, if no errors occurred.
   * @throws IOException If an error has occurred while reading/parsing the file.
   */
  Hologram load(File file) throws IOException;

  /**
   * Loads every hologram contained in the given {@code folder}.
   *
   * @param folder The folder to load the holograms from.
   * @return The list of loaded holograms, or an empty list if no holograms were found.
   * @throws IOException IF an error has occurred while reading/parsing a file.
   */
  Collection<Hologram> bulkLoad(File folder) throws IOException;

  /**
   * Checks if the given {@code file} matches this hologram loader's file type.
   *
   * @param file The file to be checked.
   * @return {@code true} if the given {@code file} matches this hologram loader's
   * file type, given by {@link #getFileType()}; {@code false} otherwise.
   */
  default boolean checkFile(File file) {
    return file.getName().endsWith(getFileType());
  }
}
