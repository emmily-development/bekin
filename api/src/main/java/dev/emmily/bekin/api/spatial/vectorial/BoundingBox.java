package dev.emmily.bekin.api.spatial.vectorial;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import xyz.xenondevs.particle.ParticleEffect;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Represents a three-dimensional bounding box in space
 * defined by two vectors: a minimum and a maximum point.
 */
public class BoundingBox {
  private static final Vector3d UP = new Vector3d(0, 1, 0);
  private static final double EPSILON = 0.000000001;

  /**
   * Creates a new bounding box approximating the area occupied
   * by an entity's custom name in Minecraft.
   * This bounding box is based on the {@code lineVector3d},
   * considering the vertical offset of the custom name
   * (assumed to be {@link HologramLine#CUSTOM_NAME_VERTICAL_OFFSET})
   * and an estimated number of letters that fit within a Minecraft block
   * (assumed to be {@link HologramLine#LETTERS_PER_BLOCK}).
   * The generated bounding box is prismatic and uniform along both X and Z axes.
   *
   * @param linePosition  The position of the line where the click occurred.
   * @param viewDirection The direction where the player is looking at.
   * @param name          The custom name of the entity, used to determine
   *                      the X and Z coordinates for the minimum and maximum
   *                      points of the bounding box.
   * @return A bounding box representing an approximate area occupied by the entity's
   * custom name in Minecraft.
   */
  public static BoundingBox fromName(String name, Vector3d linePosition, Vector3d viewDirection) {
    double textLength = (double) TextProvider.truncate(name).length() / HologramLine.LETTERS_PER_BLOCK;
    double width = textLength / 2;
    double height = HologramLine.CUSTOM_NAME_VERTICAL_OFFSET * 2;
    double depth = 0.05;

    // Calculate the points for the bounding box
    Vector3d[] points = {
      new Vector3d(-width, 0, -depth),
      new Vector3d(width, 0, -depth),
      new Vector3d(-width, height, depth),
      new Vector3d(width, height, depth)
    };

    Vector3d min = new Vector3d(Double.POSITIVE_INFINITY);
    Vector3d max = new Vector3d(Double.NEGATIVE_INFINITY);

    for (Vector3d point : points) {
      point.add(linePosition);

      min.min(point);
      max.max(point);
    }

    return new BoundingBox(min, max).rotateTowards(viewDirection);
  }



  private final Vector3d min;  // The minimum point of the bounding box.
  private final Vector3d max;  // The maximum point of the bounding box.

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
  public BoundingBox(Vector3d min,
                     Vector3d max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Gets the minimum point of the bounding box.
   *
   * @return The minimum point that defines one corner
   * of the bounding box.
   */
  public Vector3d getMin() {
    return min;
  }

  /**
   * Gets the maximum point of the bounding box.
   *
   * @return The maximum point that defines the opposite
   * corner of the bounding box.
   */
  public Vector3d getMax() {
    return max;
  }

  public BoundingBox rotateTowards(Vector3d direction) {
    direction = direction.normalize();

    double angleY = Math.atan2(direction.x(), direction.z());

    double centerX = (this.min.x + this.max.x) / 2;
    double centerZ = (this.min.z + this.max.z) / 2;

    // Rotate the minimum and maximum points around the Y-axis
    double minX = this.min.x - centerX;
    double minZ = this.min.z - centerZ;
    double maxX = this.max.x - centerX;
    double maxZ = this.max.z - centerZ;

    double rotatedMinX = minX * Math.cos(angleY) - minZ * Math.sin(angleY);
    double rotatedMinZ = minX * Math.sin(angleY) + minZ * Math.cos(angleY);
    double rotatedMaxX = maxX * Math.cos(angleY) - maxZ * Math.sin(angleY);
    double rotatedMaxZ = maxX * Math.sin(angleY) + maxZ * Math.cos(angleY);

    this.min.x = rotatedMinX + centerX;
    this.min.z = rotatedMinZ + centerZ;
    this.max.x = rotatedMaxX + centerX;
    this.max.z = rotatedMaxZ + centerZ;

    return this;
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

    System.out.printf("at %s %s %s %s %s %s", min.x, min.y, min.z, max.x, max.y, max.z);
    int id = caller.getServer().getScheduler().scheduleSyncRepeatingTask(
      caller,
      () -> {
        displayParticleLine(world, min.x, min.y, min.z, max.x, min.y, min.z, particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.x, min.y, min.z, max.x, min.y, max.z, particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.x, min.y, max.z, min.x, min.y, max.z, particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, min.x, min.y, max.z, min.x, min.y, min.z, particleCount, ParticleEffect.SMOKE_NORMAL);

        // Aristas verticales entre los vértices superiores e inferiores
        displayParticleLine(world, min.x, max.y, min.z, max.x, max.y, min.z, particleCount, ParticleEffect.FLAME);
        displayParticleLine(world, max.x, max.y, min.z, max.x, max.y, max.z, particleCount, ParticleEffect.FLAME);
        displayParticleLine(world, max.x, max.y, max.z, min.x, max.y, max.z, particleCount, ParticleEffect.FLAME);
        displayParticleLine(world, min.x, max.y, max.z, min.x, max.y, min.z, particleCount, ParticleEffect.FLAME);

        // Aristas verticales entre los vértices superiores e inferiores y las aristas horizontales
        displayParticleLine(world, min.x, min.y, min.z, min.x, max.y, min.z, particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.x, min.y, min.z, max.x, max.y, min.z, particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, max.x, min.y, max.z, max.x, max.y, max.z, particleCount, ParticleEffect.SMOKE_NORMAL);
        displayParticleLine(world, min.x, min.y, max.z, min.x, max.y, max.z, particleCount, ParticleEffect.SMOKE_NORMAL);
      },
      0,
      5 // Intervalo grande para dar la impresión de que las partículas son permanentes
    );
    caller.getServer().getScheduler().runTaskLater(caller, () -> caller.getServer().getScheduler().cancelTask(id), 100);
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
