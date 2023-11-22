package dev.emmily.bekin.api.spatial.vectorial;

import dev.emmily.bekin.api.hologram.line.HologramLine;

import java.beans.ConstructorProperties;

/**
 * Represents a three-dimensional bounding box in space
 * defined by two vectors: a minimum and a maximum point.
 */
public class BoundingBox {
  /**
   * Creates a new bounding box representing the area occupied
   * by an entity's custom name.
   * The bounding box is based on the highest point of the entity's
   * hit-box, specified by the {@code highestPoint} parameter,
   * taking into account the vertical offset of the custom name
   * (assumed to be {@link HologramLine#CUSTOM_NAME_VERTICAL_OFFSET})
   * and an estimated number of letters that fit within a Minecraft block
   * (assumed to be {@link HologramLine#LETTERS_PER_BLOCK_ESTIMATE}).
   * The created bounding box is prismatic and uniform along both X and Z axes.
   *
   * @param highestPoint The highest point of the entity's hit-box.
   * @param customName   The custom name of the entity, used to determine
   *                     the X and Z coordinates for the min and max
   *                     point.
   * @return A bounding box representing the area occupied by the entity's
   * custom name.
   */
  public static BoundingBox forEntityCustomName(Vector3D highestPoint,
                                                String customName) {
    String world = highestPoint.getWorld();

    // Represents the length (in blocks) of the bounding box minimum and
    // maximum point in both X and Z axes.
    double lengthXZ = customName.length() * HologramLine.LETTERS_PER_BLOCK_ESTIMATE;

    double relativeX = highestPoint.getX();
    double relativeY = highestPoint.getY();
    double relativeZ = highestPoint.getZ();

    double minX = relativeX - (lengthXZ / 2);
    double minY = relativeY + HologramLine.CUSTOM_NAME_VERTICAL_OFFSET;
    double minZ = relativeX - (lengthXZ / 2);

    double maxX = relativeX + (lengthXZ / 2);
    double maxY = minY + HologramLine.CUSTOM_NAME_BOX_WIDTH;
    double maxZ = relativeZ + (lengthXZ / 2);

    return new BoundingBox(
      new Vector3D(world, minX, minY, minZ),
      new Vector3D(world, maxX, maxY, maxZ)
    );
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
}