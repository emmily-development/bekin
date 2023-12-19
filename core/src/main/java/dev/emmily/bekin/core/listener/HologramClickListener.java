package dev.emmily.bekin.core.listener;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.api.spatial.raytracing.Ray;
import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.joml.Vector3d;

import java.util.List;

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
    Position origin = Position.fromBukkit(eyeLocation);

    List<Hologram> nearbyHolograms = hologramRegistry.getNearbyHolograms(origin, 5);

    if (nearbyHolograms.isEmpty()) {
      return;
    }

    double yaw = Math.toRadians(eyeLocation.getYaw());
    double pitch = Math.toRadians(eyeLocation.getPitch());

    Vector3d viewDirection = new Vector3d(
      -Math.sin(yaw) * Math.cos(pitch),
      -Math.sin(pitch),
      Math.cos(yaw) * Math.cos(pitch)
    );

    Ray ray = Ray.trace(new Vector3d(
      eyeLocation.getX(),
      eyeLocation.getY(),
      eyeLocation.getZ()
    ), viewDirection);

    for (Hologram hologram : nearbyHolograms) {
      for (HologramLine line : hologram) {
        if (!(line instanceof ClickableHologramLine)) {
          continue;
        }

        ClickableHologramLine clickableLine = (ClickableHologramLine) line;
        Position linePosition = clickableLine.getPosition();

        List<BoundingBox> boxes = BoundingBox.fromName(
          linePosition.toJOML(),
          clickableLine.getContent().apply(player)
        );

        for (BoundingBox boundingBox : boxes) {
          boundingBox.display(Bukkit.getWorld("world"), Bukkit.getPluginManager().getPlugin("bekin"));

          if (ray.intersectsBox(boundingBox)) {
            clickableLine.onClick(player);

            return;
          }
        }
      }
    }
  }
}
