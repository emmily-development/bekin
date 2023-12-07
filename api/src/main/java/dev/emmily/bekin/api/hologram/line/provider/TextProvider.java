package dev.emmily.bekin.api.hologram.line.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.line.provider.fixed.FixedTextProvider;
import org.bukkit.entity.Player;
import java.util.function.Function;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
  @JsonSubTypes.Type(FixedTextProvider.class),
})
public interface TextProvider
  extends Function<Player, String> {
  static String truncate(String text) {
    return text.length() > 256 ? text.substring(0, 256) : text;
  }

  static TextProvider staticText(String text) {
    return new FixedTextProvider(text);
  }
}
