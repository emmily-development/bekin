package dev.emmily.bekin.api.hologram.render;

import dev.emmily.bekin.api.hologram.render.empty.EmptyRenderAuthorizer;
import dev.emmily.bekin.api.hologram.render.permission.PermissionRenderAuthorizer;

public interface RenderAuthorizers {
  RenderAuthorizer EMPTY = new EmptyRenderAuthorizer();

  static RenderAuthorizer ofPermission(String permission) {
    return new PermissionRenderAuthorizer(permission);
  }

  static RenderAuthorizer empty() {
    return EMPTY;
  }
}
