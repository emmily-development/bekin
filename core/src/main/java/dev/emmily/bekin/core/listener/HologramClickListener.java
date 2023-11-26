package dev.emmily.bekin.core.listener;

import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.spatial.raytracing.Ray;
import dev.emmily.bekin.api.spatial.tree.CopyOnWritePRTree;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.bekin.api.util.lang.LanguageProvider;
import dev.emmily.sigma.api.repository.ModelRepository;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class HologramClickListener
  implements Listener {
  private final ModelRepository<CopyOnWritePRTree<ClickableHologramLine>> prTreeRepository;

  public HologramClickListener(ModelRepository<CopyOnWritePRTree<ClickableHologramLine>> prTreeRepository) {
    this.prTreeRepository = prTreeRepository;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    String world = player.getWorld().getName();

    Location eyeLocation = player.getEyeLocation();
    double yaw = Math.toRadians(eyeLocation.getYaw());
    double pitch = Math.toRadians(eyeLocation.getPitch());

    Vector3D direction = Vector3D.of(
      world,
      -Math.sin(yaw) * Math.cos(pitch),
      -Math.sin(pitch),
      Math.cos(yaw) * Math.cos(pitch)
    );

    // Trace a ray from the player's eye location to its crosshair direction
    Ray ray = Ray.trace(Vector3D.fromBukkit(world, eyeLocation.toVector()), direction);
    ray.show(player);

    String language = LanguageProvider.locale().getLanguage(player);
    CopyOnWritePRTree<ClickableHologramLine> prTree = prTreeRepository.find(language);

    if (prTree == null) {
      return;
    }

    Vector3D playerPosition = Vector3D.fromBukkit(world, player.getLocation().toVector());

    for (ClickableHologramLine line : prTree.find(direction)) {
      Vector3D linePosition = line.getPosition();
      double distance = playerPosition.distanceSquared(linePosition);

      if (distance <= 5) {
        line.onClick(player);
      }
    }
  }
}
