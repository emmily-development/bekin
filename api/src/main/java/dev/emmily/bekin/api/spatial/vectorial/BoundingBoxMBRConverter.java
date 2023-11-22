package dev.emmily.bekin.api.spatial.vectorial;

import org.khelekore.prtree.MBRConverter;

public class BoundingBoxMBRConverter
  implements MBRConverter<BoundingBox> {
  @Override
  public int getDimensions() {
    return 3;
  }

  @Override
  public double getMin(int axis,
                       BoundingBox boundingBox) {
    Vector3D min = boundingBox.getMin();

    switch (axis) {
      case 0: {
        return min.getX();
      }

      case 1: {
        return min.getY();
      }

      case 2: {
        return min.getZ();
      }

      default: throw new IllegalArgumentException("Invalid axis");
    }
  }

  @Override
  public double getMax(int axis,
                       BoundingBox boundingBox) {
    Vector3D max = boundingBox.getMax();

    switch (axis) {
      case 0: {
        return max.getX();
      }

      case 1: {
        return max.getY();
      }

      case 2: {
        return max.getZ();
      }

      default: throw new IllegalArgumentException("Invalid axis");
    }
  }
}
