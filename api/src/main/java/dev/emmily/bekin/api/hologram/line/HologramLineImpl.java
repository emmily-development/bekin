package dev.emmily.bekin.api.hologram.line;

import dev.emmily.bekin.api.hologram.line.provider.TextProvider;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.function.Function;

public class HologramLineImpl
  implements HologramLine {
  private String underlyingHologram;
  private transient int backingEntityId;
  private TextProvider content;
  private Vector3D position;
  private boolean spawned;

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
  public Vector3D getPosition() {
    return position;
  }

  @Override
  public void setPosition(Vector3D position) {
    this.position = position;
  }

  @Override
  public boolean isSpawned() {
    return spawned;
  }

  @Override
  public void setSpawned(boolean spawned) {
    this.spawned = spawned;
  }
}
