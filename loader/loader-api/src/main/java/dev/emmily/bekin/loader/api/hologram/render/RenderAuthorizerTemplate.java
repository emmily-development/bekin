package dev.emmily.bekin.loader.api.hologram.render;

import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;

import java.beans.ConstructorProperties;

/**
 * Represents a {@link RenderAuthorizer} within a configuration file.
 */
public class RenderAuthorizerTemplate {
  private final Type type;
  private final String argument;

  @ConstructorProperties({"type", "argument"})
  public RenderAuthorizerTemplate(Type type,
                                  String argument) {
    this.type = type;
    this.argument = argument;
  }

  public Type getType() {
    return type;
  }

  public String getArgument() {
    return argument;
  }

  public enum Type {
    PERMISSION
  }
}
