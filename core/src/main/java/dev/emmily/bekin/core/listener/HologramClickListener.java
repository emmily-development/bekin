package dev.emmily.bekin.core.listener;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.api.spatial.raytracing.Ray;
import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Comparator;
import java.util.List;

public class HologramClickListener implements Listener {
  private final HologramRegistry hologramRegistry;

  public HologramClickListener(HologramRegistry hologramRegistry) {
    this.hologramRegistry = hologramRegistry;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    String world = player.getWorld().getName();

    Location eyeLocation = player.getEyeLocation();
    Vector3D origin = Vector3D.fromBukkit(eyeLocation);

    List<Hologram> nearbyHolograms = hologramRegistry.getNearbyHolograms(origin, 5);

    if (nearbyHolograms.isEmpty()) {
      return;
    }

    double yaw = Math.toRadians(eyeLocation.getYaw());
    double pitch = Math.toRadians(eyeLocation.getPitch());

    Vector3D direction = Vector3D.of(
      world,
      -Math.sin(yaw) * Math.cos(pitch),
      -Math.sin(pitch),
      Math.cos(yaw) * Math.cos(pitch)
    );

    Ray ray = Ray.trace(origin, direction);

    ClickableHologramLine closestLine = null;
    double closestDistance = Double.POSITIVE_INFINITY;

    for (Hologram hologram : nearbyHolograms) {
      for (HologramLine line : hologram.getLines()) {
        if (!(line instanceof ClickableHologramLine)) {
          continue;
        }

        ClickableHologramLine clickableLine = (ClickableHologramLine) line;

        BoundingBox boundingBox = BoundingBox.fromName(
          clickableLine.getPosition(),
          clickableLine.getContent().apply(player)
        );

        double distance = ray.intersectionDistance(boundingBox);

        if (distance < closestDistance) {
          closestDistance = distance;
          closestLine = clickableLine;
        }
      }
    }

    if (closestLine != null) {
      closestLine.onClick(player);
    }
  }
}
