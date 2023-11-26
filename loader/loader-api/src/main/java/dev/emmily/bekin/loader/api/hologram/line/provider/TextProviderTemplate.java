package dev.emmily.bekin.loader.api.hologram.line.provider;

import java.beans.ConstructorProperties;

public class TextProviderTemplate {
  private final Type type;
  private final String argument;

  @ConstructorProperties({"type", "argument"})
  public TextProviderTemplate(Type type,
                              String argument) {
    this.type = type;
    this.argument = argument;
  }

  public enum Type {
    FIXED,
    I18N
  }
}
