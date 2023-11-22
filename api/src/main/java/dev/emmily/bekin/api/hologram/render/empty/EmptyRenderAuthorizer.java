package dev.emmily.bekin.api.hologram.render.empty;

import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;
import org.bukkit.entity.Player;

public class EmptyRenderAuthorizer
  implements RenderAuthorizer {
  @Override
  public boolean test(Player player) {
    return true;
  }
}
