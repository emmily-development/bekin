package dev.emmily.bekin.api.spatial.vectorial;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xenondevs.particle.ParticleEffect;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Represents a three-dimensional bounding box in space
 * defined by two vectors: a minimum and a maximum point.
 */
public class BoundingBox {
  /**
   * Creates a new bounding box approximating the area occupied
   * by an entity's custom name in Minecraft.
   * This bounding box is based on the {@code linePosition},
   * considering the vertical offset of the custom name
   * (assumed to be {@link HologramLine#CUSTOM_NAME_VERTICAL_OFFSET})
   * and an estimated number of letters that fit within a Minecraft block
   * (assumed to be {@link HologramLine#LETTERS_PER_BLOCK}).
   * The generated bounding box is prismatic and uniform along both X and Z axes.
   *
   * @param linePosition The position of the line where the click occurred.
   * @param name         The custom name of the entity, used to determine
   *                     the X and Z coordinates for the minimum and maximum
   *                     points of the bounding box.
   * @return A bounding box representing an approximate area occupied by the entity's
   *         custom name in Minecraft.
   */
  public static BoundingBox fromName(Vector3D linePosition,
                                     String name) {
    double lengthXZ = ((double) TextProvider.truncate(name).length() / HologramLine.LETTERS_PER_BLOCK) / 2;
    double offsetY = HologramLine.CUSTOM_NAME_VERTICAL_OFFSET;

    Vector3D min = linePosition.subtract(Vector3D.of(
      linePosition.getWorld(),
      lengthXZ,
      0,
      lengthXZ
    ));
    Vector3D max = linePosition.add(Vector3D.of(
      linePosition.getWorld(),
      lengthXZ,
      offsetY,
      lengthXZ
    ));

    BoundingBox box = new BoundingBox(min, max);
    box.display(Bukkit.getWorld("world"), Bukkit.getPluginManager().getPlugin("bekin"));

    return box;
  }


  private final Vector3D min;  // The minimum point of the bounding box.
  private final Vector3D max;  // The maximum point of the bounding box.

  /**
   * Constructs a new BoundingBox with the specified
   * minimum and maximum points.
   *
   * @param min The minimum point defining one corner
   *            of the bounding box.
   * @param max The maximum point defining the opposite
   *            corner of the bounding box.
   */
  @ConstructorProperties({"min", "max"})
  public BoundingBox(Vector3D min,
                     Vector3D max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Gets the minimum point of the bounding box.
   *
   * @return The minimum point that defines one corner
   * of the bounding box.
   */
  public Vector3D getMin() {
    return min;
  }

  /**
   * Gets the maximum point of the bounding box.
   *
   * @return The maximum point that defines the opposite
   * corner of the bounding box.
   */
  public Vector3D getMax() {
    return max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BoundingBox)) return false;
    BoundingBox that = (BoundingBox) o;
    return Objects.equals(min, that.min) && Objects.equals(max, that.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  public void display(World world, Plugin caller) {
    int particleCount = 1; // Ajusta la cantidad de partículas para cada arista (menos para líneas más cortas)
    int particleInterval = 0; // Ajusta la distancia entre partículas en cada arista (menos para líneas más cortas)

    caller.getServer().getScheduler().scheduleSyncRepeatingTask(
      caller,
      () -> {
        displayParticleLine(world, min.getX(), min.getY(), min.getZ(), max.getX(), min.getY(), min.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.getX(), min.getY(), min.getZ(), max.getX(), min.getY(), max.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.getX(), min.getY(), max.getZ(), min.getX(), min.getY(), max.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, min.getX(), min.getY(), max.getZ(), min.getX(), min.getY(), min.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);

        // Aristas verticales entre los vértices superiores e inferiores
        displayParticleLine(world, min.getX(), max.getY(), min.getZ(), max.getX(), max.getY(), min.getZ(), particleCount, ParticleEffect.FLAME);
        displayParticleLine(world, max.getX(), max.getY(), min.getZ(), max.getX(), max.getY(), max.getZ(), particleCount, ParticleEffect.FLAME);
        displayParticleLine(world, max.getX(), max.getY(), max.getZ(), min.getX(), max.getY(), max.getZ(), particleCount, ParticleEffect.FLAME);
        displayParticleLine(world, min.getX(), max.getY(), max.getZ(), min.getX(), max.getY(), min.getZ(), particleCount, ParticleEffect.FLAME);

        // Aristas verticales entre los vértices superiores e inferiores y las aristas horizontales
        displayParticleLine(world, min.getX(), min.getY(), min.getZ(), min.getX(), max.getY(), min.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), min.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.getX(), min.getY(), max.getZ(), max.getX(), max.getY(), max.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, min.getX(), min.getY(), max.getZ(), min.getX(), max.getY(), max.getZ(), particleCount, ParticleEffect.SMOKE_NORMAL);
      },
      0,
      5 // Intervalo grande para dar la impresión de que las partículas son permanentes
    );
  }

  private void displayParticleLine(World world, double x1, double y1, double z1, double x2, double y2, double z2, int particleCount, ParticleEffect particleEffect) {
    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    double dx = (x2 - x1) / distance;
    double dy = (y2 - y1) / distance;
    double dz = (z2 - z1) / distance;

    for (int i = 0; i <= particleCount; i++) {
      double x = x1 + dx * i * (distance / particleCount);
      double y = y1 + dy * i * (distance / particleCount);
      double z = z1 + dz * i * (distance / particleCount);
      displayParticle(world, x, y, z, particleEffect);
    }
  }


  private void displayParticle(World world, double x, double y, double z, ParticleEffect particleEffect) {
    particleEffect.display(new Location(world, x, y, z), 0, 0, 0, 0, 1, null);
  }

}
