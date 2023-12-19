package dev.emmily.bekin.api.spatial.raytracing;

import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import org.joml.Vector3d;

/**
 * Represents a three-dimensional ray defined by an origin
 * point and a normalized direction vector.
 */
public class Ray {
  public static Ray trace(Vector3d origin,
                          Vector3d direction) {
    return new Ray(origin, direction);
  }

  private final Vector3d origin;      // The origin point of the ray.
  private final Vector3d direction;   // The normalized direction vector of the ray.

  /**
   * Constructs a new Ray with the specified origin and direction.
   *
   * @param origin    The origin point of the ray.
   * @param direction A normalized direction vector indicating
   *                  the ray's direction.
   */
  public Ray(Vector3d origin,
             Vector3d direction) {
    this.origin = origin;
    this.direction = direction.normalize();  // The direction vector is normalized.
  }

  /**
   * Gets the origin point of the ray.
   *
   * @return The origin point from which the ray starts.
   */
  public Vector3d getOrigin() {
    return origin;
  }

  /**
   * Gets the normalized direction vector of the ray.
   *
   * @return The direction vector indicating the ray's direction
   * (normalized to have a length of 1).
   */
  public Vector3d getDirection() {
    return direction;
  }

  /**
   * Checks if the ray intersects with the given bounding box.
   *
   * @param box The bounding box to test for intersection.
   * @return {@code true} if the ray intersects with the bounding
   * box, {@code false} otherwise.
   */
  public boolean intersectsBox(BoundingBox box) {
    Vector3d min = box.getMin();
    Vector3d max = box.getMax();

    double tMin = (min.x() - origin.x) / direction.x;
    double tMax = (max.x() - origin.x) / direction.x;

    if (tMin > tMax) {
      double temp = tMin;
      tMin = tMax;
      tMax = temp;
    }

    double tYMin = (min.y() - origin.y) / direction.y;
    double tYMax = (max.y() - origin.y) / direction.y;

    if (tYMin > tYMax) {
      double temp = tYMin;
      tYMin = tYMax;
      tYMax = temp;
    }

    if ((tMin > tYMax) || (tYMin > tMax)) {
      return false;
    }

    if (tYMin > tMin) {
      tMin = tYMin;
    }

    if (tYMax < tMax) {
      tMax = tYMax;
    }

    double tZMin = (min.z - origin.z) / direction.z;
    double tZMax = (max.z - origin.z) / direction.z;

    if (tZMin > tZMax) {
      double temp = tZMin;
      tZMin = tZMax;
      tZMax = temp;
    }

    return (!(tMin > tZMax)) && (!(tZMin > tMax));
  }
}