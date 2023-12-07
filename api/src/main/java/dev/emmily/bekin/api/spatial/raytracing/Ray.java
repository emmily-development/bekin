package dev.emmily.bekin.api.spatial.raytracing;

import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Represents a three-dimensional ray defined by an origin
 * point and a normalized direction vector.
 */
public class Ray {
  public static Ray trace(Vector3D origin,
                          Vector3D direction) {
    return new Ray(origin, direction);
  }

  private final Vector3D origin;      // The origin point of the ray.
  private final Vector3D direction;   // The normalized direction vector of the ray.

  /**
   * Constructs a new Ray with the specified origin and direction.
   *
   * @param origin    The origin point of the ray.
   * @param direction A normalized direction vector indicating
   *                  the ray's direction.
   */
  public Ray(Vector3D origin,
             Vector3D direction) {
    this.origin = origin;
    this.direction = direction.normalize();  // The direction vector is normalized.
  }

  /**
   * Gets the origin point of the ray.
   *
   * @return The origin point from which the ray starts.
   */
  public Vector3D getOrigin() {
    return origin;
  }

  /**
   * Gets the normalized direction vector of the ray.
   *
   * @return The direction vector indicating the ray's direction
   * (normalized to have a length of 1).
   */
  public Vector3D getDirection() {
    return direction;
  }

  /**
   * Calculates the distance at which the ray intersects with the given bounding box.
   *
   * @param box The bounding box to test for intersection.
   * @return The distance along the ray's direction vector to the point of intersection
   *         with the bounding box. Returns {@link Double#POSITIVE_INFINITY} if there
   *         is no intersection.
   */
  public double intersectionDistance(BoundingBox box) {
    Vector3D min = box.getMin();
    Vector3D max = box.getMax();

    double tMin = (min.getX() - origin.getX()) / direction.getX();
    double tMax = (max.getX() - origin.getX()) / direction.getX();

    if (tMin > tMax) {
      double temp = tMin;
      tMin = tMax;
      tMax = temp;
    }

    double tYMin = (min.getY() - origin.getY()) / direction.getY();
    double tYMax = (max.getY() - origin.getY()) / direction.getY();

    if (tYMin > tYMax) {
      double temp = tYMin;
      tYMin = tYMax;
      tYMax = temp;
    }

    if ((tMin > tYMax) || (tYMin > tMax)) {
      return Double.POSITIVE_INFINITY;
    }

    if (tYMin > tMin) {
      tMin = tYMin;
    }

    if (tYMax < tMax) {
      tMax = tYMax;
    }

    double tZMin = (min.getZ() - origin.getZ()) / direction.getZ();
    double tZMax = (max.getZ() - origin.getZ()) / direction.getZ();

    if (tZMin > tZMax) {
      double temp = tZMin;
      tZMin = tZMax;
      tZMax = temp;
    }

    if ((tMin > tZMax) || (tZMin > tMax)) {
      return Double.POSITIVE_INFINITY;
    }

    double t = Math.max(Math.max(tMin, tYMin), tZMin);
    return t >= 0 ? t : Double.POSITIVE_INFINITY;
  }

  public void show(Player player) {
    Location playerLocation = player.getLocation();
    Vector bukkitVector = direction.toBukkit();

    Arrow arrow = player.getWorld().spawnArrow(playerLocation, bukkitVector, 1.0f, 1.0f);
    arrow.setVelocity(bukkitVector);
    arrow.setShooter(player);
  }
}