package dev.emmily.bekin.api.hologram.handler;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static dev.emmily.bekin.api.util.protocol.ProtocolConstants.SEMANTIC_SERVER_VERSION;
import static dev.emmily.bekin.api.util.protocol.ProtocolConstants.SERVER_VERSION;

/**
 * Defines the methods for rendering and managing holograms for
 * specific players.
 */
public interface HologramHandler {
  static HologramHandler getInstance(Namespace namespace) {
    return Holder.getInstance(namespace);
  }

  /**
   * Spawns a hologram in its corresponding world.
   *
   * @param hologram The hologram to be spawned.
   */
  void spawn(Hologram hologram);

  /**
   * Destroys a hologram, removing it from the world.
   *
   * @param hologram The hologram to be destroyed.
   */
  void destroy(Hologram hologram);

  /**
   * Renders a hologram for a specific player.
   *
   * @param hologram The hologram to be rendered.
   * @param player   The player for whom the hologram should be rendered.
   */
  void render(Hologram hologram, Player player);

  default void renderForEveryone(Hologram hologram) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      render(hologram, player);
    }
  }

  /**
   * Hides a hologram for a specific player.
   *
   * @param hologram The hologram to be hidden.
   * @param player   The player for whom the hologram should be hidden.
   */
  void hide(Hologram hologram,
            Player player);

  default void hideFromEveryone(Hologram hologram) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      hide(hologram, player);
    }
  }

  /**
   * Moves the hologram to a given position.
   *
   * @param hologram    The hologram to be moved.
   * @param newPosition The position to move the hologram to.
   */
  void move(Hologram hologram,
            Vector3D newPosition);

  static IllegalArgumentException unknownEntity(Hologram hologram,
                                                HologramLine line) {
    return new IllegalArgumentException(String.format(
      "The armor stand of the line %s of the hologram %s couldn't be found. Did you use the kill command?",
      hologram.getLines().indexOf(line),
      hologram.getId()
    ));
  }

  enum Namespace {
    ARMOR_STAND,
    TEXT_DISPLAY
  }

  class Holder {
    private static volatile Map<Namespace, HologramHandler> instances = new HashMap<>();
    private static final Object LOCK = new Object();
    private static final String PROTOCOL_CLASS_PATTERN = "dev.emmily.bekin.protocol.%s.%s";

    static HologramHandler getInstance(Namespace namespace) {
      if (instances.containsKey(namespace)) {
        return instances.get(namespace);
      }

      synchronized (LOCK) {
        if (instances.get(namespace) == null) {
          try {
            String className = null;

            if (SEMANTIC_SERVER_VERSION.getMinor() < 19) {
              className = "HologramHandlerImpl";
            } else {
              if (namespace == Namespace.ARMOR_STAND) {
                className = "ArmorStandHologramHandler";
              } else if (namespace == Namespace.TEXT_DISPLAY) {
                className = "TextDisplayHologramHandler";
              }
            }

            HologramHandler hologramHandler = (HologramHandler) Class
              .forName(String.format(PROTOCOL_CLASS_PATTERN, SERVER_VERSION, className))
              .getConstructor()
              .newInstance();
            instances.put(namespace, hologramHandler);
          } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(
              "your server version (%s) is not supported by bekin",
              SERVER_VERSION
            ));
          } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      }

      return instances.get(namespace);
    }
  }
}
