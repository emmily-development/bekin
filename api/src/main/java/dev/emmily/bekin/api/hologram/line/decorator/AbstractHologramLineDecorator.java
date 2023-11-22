package dev.emmily.bekin.api.hologram.line.decorator;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;

public abstract class AbstractHologramLineDecorator
  implements HologramLine {
  private final HologramLine wrappedLine;

  public AbstractHologramLineDecorator(HologramLine wrappedLine) {
    this.wrappedLine = wrappedLine;
  }

  @Override
  public TextProvider getContent() {
    return wrappedLine.getContent();
  }

  @Override
  public void setContent(TextProvider content) {
    wrappedLine.setContent(content);
  }

  @Override
  public Vector3D getPosition() {
    return wrappedLine.getPosition();
  }

  @Override
  public void setPosition(Vector3D position) {
    wrappedLine.setPosition(position);
  }

  @Override
  public int getBackingEntityId() {
    return wrappedLine.getBackingEntityId();
  }

  @Override
  public void setBackingEntityId(int backingEntityId) {
    wrappedLine.setBackingEntityId(backingEntityId);
  }

  @Override
  public boolean isSpawned() {
    return wrappedLine.isSpawned();
  }

  @Override
  public void setSpawned(boolean spawned) {
    wrappedLine.setSpawned(spawned);
  }
}
