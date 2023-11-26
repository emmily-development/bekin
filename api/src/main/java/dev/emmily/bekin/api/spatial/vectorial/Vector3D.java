package dev.emmily.bekin.api.spatial.vectorial;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.beans.ConstructorProperties;

/**
 * Represents a three-dimensional vector with
 * floating-point components (x, y, z) in a
 * specific world.
 * It provides various vector operations and methods
 * for common vector manipulations.
 */
public class Vector3D {
  public static Vector3D of(String world,
                            double x,
                            double y,
                            double z) {
    return new Vector3D(world, x, y, z);
  }

  public static Vector3D fromBukkit(String world,
                                    Vector bukkit) {
    return new Vector3D(
      world,
      bukkit.getX(),
      bukkit.getY(),
      bukkit.getZ()
    );
  }

  public static Vector3D fromBukkit(Location location) {
    return new Vector3D(
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

  /**
   * Constructs a new `Vector3F` with the specified world,
   * x, y, and z components.
   *
   * @param world The world associated with the vector.
   * @param x     The x-component of the vector.
   * @param y     The y-component of the vector.
   * @param z     The z-component of the vector.
   */
  @ConstructorProperties({
    "world", "x", "y", "z"
  })
  public Vector3D(String world,
                  double x,
                  double y,
                  double z) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Gets the world associated with the vector.
   *
   * @return The world name.
   */
  public String getWorld() {
    return world;
  }

  /**
   * Gets the x-component of the vector.
   *
   * @return The x-component.
   */
  public double getX() {
    return x;
  }

  /**
   * Gets the y-component of the vector.
   *
   * @return The y-component.
   */
  public double getY() {
    return y;
  }

  /**
   * Gets the z-component of the vector.
   *
   * @return The z-component.
   */
  public double getZ() {
    return z;
  }

  /**
   * Calculates and returns the magnitude (length) of the vector.
   *
   * @return The magnitude of the vector.
   */
  public double magnitude() {
    return (double) Math.sqrt(x * x + y * y + z * z);
  }

  /**
   * Calculates and returns the dot product between this
   * vector and another vector.
   *
   * @param other The other vector to compute the dot product with.
   * @return The dot product of the two vectors.
   */
  public double dot(Vector3D other) {
    return x * other.getX() + y * other.getY() + z * other.getZ();
  }

  /**
   * Calculates and returns the cross product between
   * this vector and another vector.
   *
   * @param other The other vector to compute the cross product with.
   * @return The resulting vector from the cross product.
   */
  public Vector3D cross(Vector3D other) {
    double resultX = y * other.getZ() - z * other.getY();
    double resultY = z * other.getX() - x * other.getZ();
    double resultZ = x * other.getY() - y * other.getX();

    return new Vector3D(world, resultX, resultY, resultZ);
  }

  /**
   * Adds another vector to this vector and returns the
   * result as a new vector.
   *
   * @param other The vector to add to this vector.
   * @return The resulting vector from the addition.
   */
  public Vector3D add(Vector3D other) {
    double resultX = x + other.getX();
    double resultY = y + other.getY();
    double resultZ = z + other.getZ();

    return new Vector3D(world, resultX, resultY, resultZ);
  }

  /**
   * Subtracts another vector from this vector and returns the
   * result as a new vector.
   *
   * @param other The vector to subtract from this vector.
   * @return The resulting vector from the subtraction.
   */
  public Vector3D subtract(Vector3D other) {
    double resultX = x - other.getX();
    double resultY = y - other.getY();
    double resultZ = z - other.getZ();

    return new Vector3D(world, resultX, resultY, resultZ);
  }

  /**
   * Normalizes the vector, converting it into a unit vector
   * with the same direction.
   *
   * @return A new normalized Vector3F representing the unit
   * vector in the same direction as the original vector.
   */
  public Vector3D normalize() {
    double magnitude = magnitude();

    if (magnitude != 0) {
      double inverseMagnitude = 1.0f / magnitude;
      double resultX = x * inverseMagnitude;
      double resultY = y * inverseMagnitude;
      double resultZ = z * inverseMagnitude;

      return new Vector3D(world, resultX, resultY, resultZ);
    } else {
      // Handle division by zero (undefined normalization)
      return new Vector3D(world, x, y, z);
    }
  }

  /**
   * Calculates the squared Euclidean distance between this vector and the given {@code beta} vector.
   * The squared Euclidean distance represents the square of the Euclidean distance and is calculated as:
   * d(p, q)^2 = (q1 - p1)^2 + (q2 - p2)^2 + (q3 - p3)^2
   *
   * @param beta The vector to which the squared distance is calculated.
   * @return The squared Euclidean distance between this vector and the given {@code beta} vector.
   *         It represents the square of the Euclidean distance in a three-dimensional space.
   */
  public double distanceSquared(Vector3D beta) {
    double deltaX = beta.getX() - getX();
    double deltaY = beta.getY() - getY();
    double deltaZ = beta.getZ() - getZ();

    return Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2);
  }

  public Vector toBukkit() {
    return new Vector(x, y, z);
  }

  @Override
  public String toString() {
    return "Vector3D{" +
      "world='" + world + '\'' +
      ", x=" + x +
      ", y=" + y +
      ", z=" + z +
      '}';
  }
}
