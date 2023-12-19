package dev.emmily.bekin.api.spatial.vectorial;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.joml.Vector3d;

import java.beans.ConstructorProperties;
import java.util.Objects;

/**
 * Represents a three-dimensional position with
 * floating-point components (x, y, z) in a
 * specific world.
 * It provides various position operations and methods
 * for common position manipulations.
 */
public class Position {
  public static Position of(String world,
                            double x,
                            double y,
                            double z) {
    return new Position(world, x, y, z);
  }

  public static Position fromBukkit(String world,
                                    Vector bukkit) {
    return of(
      world,
      bukkit.getX(),
      bukkit.getY(),
      bukkit.getZ()
    );
  }

  public static Position fromBukkit(Location location) {
    return of(
      location.getWorld().getName(),
      location.getX(),
      location.getY(),
      location.getZ()
    );
  }

  private final String world;
  private final double x;
  private final double y;
  private final double z;
  private final float yaw;

  /**
   * Constructs a new `position3F` with the specified world,
   * x, y, and z components.
   *
   * @param world The world associated with the position
   * @param x     The x-component of the position
   * @param y     The y-component of the position
   * @param z     The z-component of the position
   * @param yaw   The yaw component of the position.
   */
  @ConstructorProperties({
    "world", "x", "y", "z"
  })
  public Position(String world,
                  double x,
                  double y,
                  double z,
                  float yaw) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
  }

  public Position(String world,
                  double x,
                  double y,
                  double z) {
    this(world, x, y, z, 0);
  }

  /**
   * Gets the world associated with the position
   *
   * @return The world name.
   */
  public String getWorld() {
    return world;
  }

  /**
   * Gets the x-component of the position
   *
   * @return The x-component.
   */
  public double getX() {
    return x;
  }

  /**
   * Gets the y-component of the position
   *
   * @return The y-component.
   */
  public double getY() {
    return y;
  }

  /**
   * Gets the z-component of the position
   *
   * @return The z-component.
   */
  public double getZ() {
    return z;
  }

  public float getYaw() {
    return yaw;
  }

  /**
   * Calculates and returns the magnitude (length) of the position
   *
   * @return The magnitude of the position
   */
  public double magnitude() {
    return Math.sqrt(x * x + y * y + z * z);
  }

  /**
   * Calculates and returns the dot product between this
   * position and another position
   *
   * @param other The other position to compute the dot product with.
   * @return The dot product of the two positions.
   */
  public double dot(Position other) {
    return x * other.getX() + y * other.getY() + z * other.getZ();
  }

  /**
   * Calculates and returns the cross product between
   * this position and another position
   *
   * @param other The other position to compute the cross product with.
   * @return The resulting position from the cross product.
   */
  public Position cross(Position other) {
    double resultX = y * other.getZ() - z * other.getY();
    double resultY = z * other.getX() - x * other.getZ();
    double resultZ = x * other.getY() - y * other.getX();

    return of(world, resultX, resultY, resultZ);
  }

  /**
   * Adds another position to this position and returns the
   * result as a new position
   *
   * @param other The position to add to this position
   * @return The resulting position from the addition.
   */
  public Position add(Position other) {
    double resultX = x + other.getX();
    double resultY = y + other.getY();
    double resultZ = z + other.getZ();

    return of(world, resultX, resultY, resultZ);
  }

  /**
   * Subtracts another position from this position and returns the
   * result as a new position
   *
   * @param other The position to subtract from this position
   * @return The resulting position from the subtraction.
   */
  public Position subtract(Position other) {
    double resultX = x - other.getX();
    double resultY = y - other.getY();
    double resultZ = z - other.getZ();

    return of(world, resultX, resultY, resultZ);
  }

  /**
   * Normalizes the position, converting it into a unit position
   * with the same direction.
   *
   * @return A new normalized position3F representing the unit
   * position in the same direction as the original position
   */
  public Position normalize() {
    double magnitude = magnitude();

    if (magnitude != 0) {
      double inverseMagnitude = 1.0f / magnitude;
      double resultX = x * inverseMagnitude;
      double resultY = y * inverseMagnitude;
      double resultZ = z * inverseMagnitude;

      return of(world, resultX, resultY, resultZ);
    } else {
      // Handle division by zero (undefined normalization)
      return of(world, x, y, z);
    }
  }

  /**
   * Calculates the squared Euclidean distance between this position and the given {@code beta} position
   * The squared Euclidean distance represents the square of the Euclidean distance and is calculated as:
   * d(p, q)^2 = (q1 - p1)^2 + (q2 - p2)^2 + (q3 - p3)^2
   *
   * @param beta The position to which the squared distance is calculated.
   * @return The squared Euclidean distance between this position and the given {@code beta} position
   * It represents the square of the Euclidean distance in a three-dimensional space.
   */
  public double distanceSquared(Position beta) {
    double deltaX = beta.getX() - getX();
    double deltaY = beta.getY() - getY();
    double deltaZ = beta.getZ() - getZ();

    return Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2);
  }

  /**
   * Multiplies each component of the position by a scalar value
   * and returns the resulting position
   *
   * @param scalar The scalar value to multiply by.
   * @return A new position resulting from the multiplication operation.
   */
  public Position multiply(double scalar) {
    double resultX = x * scalar;
    double resultY = y * scalar;
    double resultZ = z * scalar;

    return of(world, resultX, resultY, resultZ);
  }

  public Vector toBukkit() {
    return new Vector(x, y, z);
  }

  public Location toLocation() {
    return new Location(Bukkit.getWorld(world), x, y, z);
  }

  public Vector3d toJOML() {
    return new Vector3d(x, y, z);
  }

  @Override
  public String toString() {
    return "position3D{" +
      "world='" + world + '\'' +
      ", x=" + x +
      ", y=" + y +
      ", z=" + z +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Position)) return false;
    Position position = (Position) o;
    return Double.compare(x, position.x) == 0
      && Double.compare(y, position.y) == 0
      && Double.compare(z, position.z) == 0
      && Float.compare(yaw, position.yaw) == 0
      && Objects.equals(world, position.world);
  }

  @Override
  public int hashCode() {
    return Objects.hash(world, x, y, z, yaw);
  }
}
