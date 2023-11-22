package dev.emmily.bekin.api.hologram.render.permission;

import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;
import org.bukkit.entity.Player;

public class PermissionRenderAuthorizer
  implements RenderAuthorizer {
  private final String permission;

  public PermissionRenderAuthorizer(String permission) {
    this.permission = permission;
  }

  @Override
  public boolean test(Player player) {
    return player.hasPermission(permission);
  }
}
