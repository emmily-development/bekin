package dev.emmily.bekin.api.hologram.line.decorator.click.action;

import com.cryptomorin.xseries.XSound;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.item.ItemClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.message.SendMessageClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.sound.PlaySoundClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.teleport.TeleportClickAction;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
  @JsonSubTypes.Type(ItemClickAction.class),
  @JsonSubTypes.Type(SendMessageClickAction.class),
  @JsonSubTypes.Type(PlaySoundClickAction.class),
  @JsonSubTypes.Type(TeleportClickAction.class)
})
public interface HologramClickAction
  extends Consumer<Player> {
  static HologramClickAction item(ItemStack item,
                                  ItemClickAction.Operation operation) {
    return new ItemClickAction(item, operation);
  }

  static HologramClickAction sendMessage(String message) {
    return new SendMessageClickAction(message);
  }

  static HologramClickAction playSound(XSound sound,
                                       float volume,
                                       float pitch) {
    return new PlaySoundClickAction(sound, volume, pitch);
  }

  static HologramClickAction teleport(Position to) {
    return new TeleportClickAction(to);
  }
}
