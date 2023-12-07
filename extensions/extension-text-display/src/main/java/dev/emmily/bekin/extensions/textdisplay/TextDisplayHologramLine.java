package dev.emmily.bekin.extensions.textdisplay;

import dev.emmily.bekin.api.hologram.line.HologramLineImpl;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.hologram.line.provider.fixed.FixedTextProvider;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;

public class TextDisplayHologramLine
  extends HologramLineImpl {
  private final TextDisplay.TextAlignment textAlignment;
  private final Color backgroundColor;
  private final int lineWidth;
  private final byte textOpacity;
  private final boolean defaultBackground;
  private final boolean seeThrough;
  private final boolean shadowed;
  private final Transformation transformation;
  private final int interpolationDuration;
  private final float shadowRadius;
  private final float shadowStrength;
  private final float displayHeight;
  private final int interpolationDelay;
  private final Color glowColorOverride;
  private final Display.Brightness brightness;
  private final Display.Billboard billboard;

  public TextDisplayHologramLine(TextProvider content,
                                 TextDisplay.TextAlignment textAlignment,
                                 Color backgroundColor,
                                 int lineWidth,
                                 byte textOpacity,
                                 boolean defaultBackground,
                                 boolean seeThrough,
                                 boolean shadowed,
                                 Transformation transformation,
                                 int interpolationDuration,
                                 float shadowRadius,
                                 float shadowStrength,
                                 float displayHeight,
                                 int interpolationDelay,
                                 Color glowColorOverride,
                                 Display.Brightness brightness,
                                 Display.Billboard billboard) {
    super(content);

    if (!(content instanceof FixedTextProvider)) {
      throw new IllegalArgumentException("Text displays only support static text!");
    }

    this.textAlignment = textAlignment;
    this.backgroundColor = backgroundColor;
    this.lineWidth = lineWidth;
    this.textOpacity = textOpacity;
    this.defaultBackground = defaultBackground;
    this.seeThrough = seeThrough;
    this.shadowed = shadowed;
    this.transformation = transformation;
    this.interpolationDuration = interpolationDuration;
    this.shadowRadius = shadowRadius;
    this.shadowStrength = shadowStrength;
    this.displayHeight = displayHeight;
    this.interpolationDelay = interpolationDelay;
    this.glowColorOverride = glowColorOverride;
    this.brightness = brightness;
    this.billboard = billboard;
  }

  public TextDisplay.TextAlignment getTextAlignment() {
    return textAlignment;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public int getLineWidth() {
    return lineWidth;
  }

  public byte getTextOpacity() {
    return textOpacity;
  }

  public boolean isDefaultBackground() {
    return defaultBackground;
  }

  public boolean isSeeThrough() {
    return seeThrough;
  }

  public boolean isShadowed() {
    return shadowed;
  }

  public Transformation getTransformation() {
    return transformation;
  }

  public int getInterpolationDuration() {
    return interpolationDuration;
  }

  public float getShadowRadius() {
    return shadowRadius;
  }

  public float getShadowStrength() {
    return shadowStrength;
  }

  public float getDisplayHeight() {
    return displayHeight;
  }

  public int getInterpolationDelay() {
    return interpolationDelay;
  }

  public Color getGlowColorOverride() {
    return glowColorOverride;
  }

  public Display.Brightness getBrightness() {
    return brightness;
  }

  public Display.Billboard getBillboard() {
    return billboard;
  }
}
