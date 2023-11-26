package dev.emmily.bekin.api.hologram.spatial;

import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.khelekore.prtree.MBRConverter;

public class HologramMBRConverter
  implements MBRConverter<ClickableHologramLine> {
  public static HologramMBRConverter forLanguage(String language) {
    return new HologramMBRConverter(language);
  }

  private final String language;

  public HologramMBRConverter(String language) {
    this.language = language;
  }

  @Override
  public int getDimensions() {
    return 3;
  }

  @Override
  public double getMin(int axis,
                       ClickableHologramLine clickableHologramLine) {
    return getValueFromVector(axis, clickableHologramLine.getBoundingBox(language).getMin());
  }

  @Override
  public double getMax(int axis,
                       ClickableHologramLine clickableHologramLine) {
    return getValueFromVector(axis, clickableHologramLine.getBoundingBox(language).getMax());
  }

  private double getValueFromVector(int axis,
                                    Vector3D vector) {
    switch (axis) {
      case 0: {
        return vector.getX();
      }

      case 1: {
        return vector.getY();
      }

      case 2: {
        return vector.getZ();
      }

      default: throw new IllegalArgumentException("Invalid axis");
    }
  }
}
