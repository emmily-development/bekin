package dev.emmily.bekin.api.hologram.line;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLineDecorator;
import dev.emmily.bekin.api.hologram.line.decorator.update.UpdatableHologramLineDecorator;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
  @JsonSubTypes.Type(HologramLineImpl.class),
  @JsonSubTypes.Type(UpdatableHologramLineDecorator.class),
  @JsonSubTypes.Type(ClickableHologramLineDecorator.class)
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
   * The width of a custom name box in blocks.
   */
  float CUSTOM_NAME_BOX_WIDTH = 0.25f;

  /**
   * The estimated number of letters required to represent a block in length (approximately).
   * This constant is used to calculate bounding box dimensions based on the length of the custom text.
   */
  int LETTERS_PER_BLOCK_ESTIMATE = 5;

  int getBackingEntityId();

  void setBackingEntityId(int backingEntityId);

  TextProvider getContent();

  void setContent(TextProvider content);

  Vector3D getPosition();

  void setPosition(Vector3D position);

  boolean isSpawned();

  void setSpawned(boolean spawned);
}
