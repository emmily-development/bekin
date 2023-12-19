package dev.emmily.bekin.api.hologram.line.decorator;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Position;

public abstract class AbstractLineDecorator
  implements HologramLine {
  private final HologramLine wrappedLine;

  public AbstractLineDecorator(HologramLine wrappedLine) {
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
  public Position getPosition() {
    return wrappedLine.getPosition();
  }

  @Override
  public void setPosition(Position position) {
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
  public String getUnderlyingHologram() {
    return wrappedLine.getUnderlyingHologram();
  }

  @Override
  public void setUnderlyingHologram(String underlyingHologram) {
    wrappedLine.setUnderlyingHologram(underlyingHologram);
  }

  public HologramLine getWrappedLine() {
    return wrappedLine;
  }
}
