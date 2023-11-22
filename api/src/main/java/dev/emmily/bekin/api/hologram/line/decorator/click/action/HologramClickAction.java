package dev.emmily.bekin.api.hologram.line.decorator.click.action;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.item.ItemClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.message.SendMessageClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.sound.PlaySoundClickAction;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.teleport.TeleportClickAction;
import org.bukkit.entity.Player;

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
}
