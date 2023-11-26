package dev.emmily.bekin.api.hologram.render;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.render.empty.EmptyRenderAuthorizer;
import dev.emmily.bekin.api.hologram.render.permission.PermissionRenderAuthorizer;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
  @JsonSubTypes.Type(PermissionRenderAuthorizer.class),
  @JsonSubTypes.Type(EmptyRenderAuthorizer.class)
})
public interface RenderAuthorizer
  extends Predicate<Player> {
  RenderAuthorizer EMPTY = new EmptyRenderAuthorizer();

  static RenderAuthorizer ofPermission(String permission) {
    return new PermissionRenderAuthorizer(permission);
  }

  static RenderAuthorizer empty() {
    return EMPTY;
  }
}
