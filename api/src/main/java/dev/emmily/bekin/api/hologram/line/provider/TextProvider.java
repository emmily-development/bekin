package dev.emmily.bekin.api.hologram.line.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.line.provider.fixed.StaticTextProvider;
import dev.emmily.bekin.api.hologram.line.provider.i18n.MultiLanguageTextProvider;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
  @JsonSubTypes.Type(StaticTextProvider.class),
  @JsonSubTypes.Type(MultiLanguageTextProvider.class)
})
public interface TextProvider
  extends BiFunction<Player, MessageHandler, String> {
}
