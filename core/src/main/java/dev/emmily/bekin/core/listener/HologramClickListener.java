package dev.emmily.bekin.core.listener;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLineDecorator;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import dev.emmily.bekin.api.spatial.raytracing.Ray;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class HologramClickListener
  implements Listener {
  private final HologramRegistry hologramRegistry;

  public HologramClickListener(HologramRegistry hologramRegistry) {
    this.hologramRegistry = hologramRegistry;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    String world = player.getWorld().getName();

    Location eyeLocation = player.getEyeLocation();
    double yaw = Math.toRadians(eyeLocation.getYaw());
    double pitch = Math.toRadians(eyeLocation.getPitch());

    Vector3D direction = new Vector3D(
      world,
      -Math.sin(yaw) * Math.cos(pitch),
      -Math.sin(pitch),
      Math.cos(yaw) * Math.cos(pitch)
    );

    // Trace a ray from the player's eye location to its crosshair direction
    Ray ray = Ray.trace(Vector3D.fromBukkit(world, eyeLocation.toVector()), direction);
    ray.show(player);

    for (Hologram hologram : hologramRegistry.getAll()) {
      for (HologramLine line : hologram) {
        if (!(line instanceof ClickableHologramLineDecorator)) {
          continue;
        }

        ClickableHologramLineDecorator clickableLine = (ClickableHologramLineDecorator) line;
        BoundingBox boundingBox = clickableLine.getBoundingBox(player);

        if (boundingBox == null) {
          // Should never happen
          continue;
        }

        if (ray.intersectsBox(boundingBox)) {
          clickableLine.onClick(player);

          break;
        }
      }
    }
  }
}
