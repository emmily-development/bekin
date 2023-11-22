package dev.emmily.bekin.api.hologram.line.decorator.click.action.message;

import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public class SendMessageClickAction
  implements HologramClickAction {
  private final String message;

  @ConstructorProperties("message")
  public SendMessageClickAction(String message) {
    this.message = message;
  }

  @Override
  public void accept(Player player) {
    player.sendMessage(message);
  }
}
