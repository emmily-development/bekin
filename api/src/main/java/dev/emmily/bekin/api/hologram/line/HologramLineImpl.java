package dev.emmily.bekin.api.hologram.line;

import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Position;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class HologramLineImpl
  implements HologramLine {
  private String underlyingHologram;
  private transient int backingEntityId;
  private TextProvider content;
  private Position position;

  @ConstructorProperties("content")
  public HologramLineImpl(TextProvider content) {
    this.content = content;
  }

  @Override
  public String getUnderlyingHologram() {
    return underlyingHologram;
  }

  @Override
  public void setUnderlyingHologram(String underlyingHologram) {
    this.underlyingHologram = underlyingHologram;
  }

  @Override
  public int getBackingEntityId() {
    return backingEntityId;
  }

  @Override
  public void setBackingEntityId(int backingEntityId) {
    this.backingEntityId = backingEntityId;
  }

  @Override
  public TextProvider getContent() {
    return content;
  }

  @Override
  public void setContent(TextProvider content) {
    this.content = content;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public void setPosition(Position position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HologramLineImpl)) return false;
    HologramLineImpl that = (HologramLineImpl) o;
    return backingEntityId == that.backingEntityId
      && Objects.equals(underlyingHologram, that.underlyingHologram)
      && Objects.equals(position, that.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      underlyingHologram,
      backingEntityId,
      content, position
    );
  }
}
