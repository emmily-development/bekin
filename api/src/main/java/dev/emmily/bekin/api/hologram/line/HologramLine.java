package dev.emmily.bekin.api.hologram.line;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.update.UpdatableHologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a holographic line within a hologram.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
  @JsonSubTypes.Type(HologramLineImpl.class),
  @JsonSubTypes.Type(UpdatableHologramLine.class),
  @JsonSubTypes.Type(ClickableHologramLine.class)
})
public interface HologramLine {
  static HologramLine line(TextProvider content) {
    return new HologramLineImpl(content);
  }

  /**
   * The vertical offset between the top of an armor stand's hit-box and its custom name.
   */
  float CUSTOM_NAME_VERTICAL_OFFSET = 0.25f;

  /**
   * The number of uppercase letters that represent a 1 block length.
   * This constant is used to calculate bounding box dimensions based on the length of the custom text.
   */
  int LETTERS_PER_BLOCK = 7;

  String getUnderlyingHologram();

  @ApiStatus.Internal
  void setUnderlyingHologram(String underlyingHologram);

  /**
   * Returns the line's backing armor stand id.
   *
   * @return The line's backing armor stand id.
   */
  int getBackingEntityId();

  @ApiStatus.Internal
  void setBackingEntityId(int backingEntityId);

  /**
   * Returns the function that is applied to show this line to
   * a given player.
   *
   * @return The function that is applied to show this line to
   * a given player.
   */
  TextProvider getContent();

  @ApiStatus.Internal
  void setContent(TextProvider content);

  /**
   * Returns the current position of the line.
   *
   * @return The current position of the line.
   */
  Vector3D getPosition();

  @ApiStatus.Internal
  void setPosition(Vector3D position);
}
