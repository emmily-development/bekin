package dev.emmily.bekin.api.hologram.handler;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.spatial.PrTreeRegistry;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Defines the methods for rendering and managing holograms for
 * specific players.
 */
public interface HologramHandler {
  static HologramHandler getInstance(MessageHandler messageHandler,
                                     PrTreeRegistry prTreeRegistry) {
    return Holder.getInstance(messageHandler, prTreeRegistry);
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

  void renderForEveryone(Hologram hologram);

  /**
   * Hides a hologram for a specific player.
   *
   * @param hologram The hologram to be hidden.
   * @param player   The player for whom the hologram should be hidden.
   */
  void hide(Hologram hologram,
            Player player);

  void hideFromEveryone(Hologram hologram);

  /**
   * Moves the hologram to a given location.
   *
   * @param hologram    The hologram to be moved.
   * @param newPosition The position to move the hologram to.
   */
  void move(Hologram hologram,
            Vector3D newPosition);

  class Holder {
    private static volatile HologramHandler instance;
    private static final Object LOCK = new Object();
    private static final String PROTOCOL_CLASS_PATTERN = "dev.emmily.bekin.protocol.%s";
    private static final String SERVER_VERSION = Bukkit
      .getServer()
      .getClass()
      .getName()
      .split("\\.")[3];

    static HologramHandler getInstance(MessageHandler messageHandler,
                                       PrTreeRegistry prTreeRegistry) {
      if (instance != null) {
        return instance;
      }

      synchronized (LOCK) {
        if (instance == null) {
          try {
            instance = (HologramHandler) Class
              .forName(String.format(PROTOCOL_CLASS_PATTERN, SERVER_VERSION + ".HologramHandlerImpl"))
              .getConstructor(MessageHandler.class, PrTreeRegistry.class)
              .newInstance(messageHandler, prTreeRegistry);
          } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("your server version (%s) is not supported by bekin", SERVER_VERSION));
          } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      }

      return instance;
    }
  }
}
