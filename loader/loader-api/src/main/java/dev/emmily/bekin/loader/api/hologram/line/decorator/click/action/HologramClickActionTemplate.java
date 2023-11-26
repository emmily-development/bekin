package dev.emmily.bekin.loader.api.hologram.line.decorator.click.action;

import java.beans.ConstructorProperties;

public class HologramClickActionTemplate {
  private final Type type;
  private final String argument;

  @ConstructorProperties({"type", "argument"})
  public HologramClickActionTemplate(Type type,
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
    RUN_COMMAND,
    PLAY_SOUND,
    SEND_MESSAGE,
    GIVE_ITEM,
    TELEPORT
  }
}
