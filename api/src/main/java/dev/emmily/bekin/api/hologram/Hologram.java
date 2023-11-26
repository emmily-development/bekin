package dev.emmily.bekin.api.hologram;

import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.sigma.api.Model;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a collection of holographic lines,
 * allowing you to create and manage multiple
 * lines of text or items as a single holographic
 * display within the game.
 */
public class Hologram
  implements Model, Iterable<HologramLine> {
  public static Builder builder() {
    return new Builder();
  }

  private final String id;
  private Vector3D position;
  private final List<HologramLine> lines;
  private RenderAuthorizer renderAuthorizer;
  private int renderDistance;
  private final transient List<String> viewers;

  @ConstructorProperties({
    "id", "position", "lines",
    "renderAuthorizer",
    "renderDistance"
  })
  public Hologram(String id,
                  Vector3D position,
                  List<HologramLine> lines,
                  RenderAuthorizer renderAuthorizer,
                  int renderDistance) {
    this.id = id;
    this.position = position;
    this.lines = lines;
    this.renderAuthorizer = renderAuthorizer;
    this.renderDistance = renderDistance;
    this.viewers = new ArrayList<>();

    this.lines.forEach(line -> line.setUnderlyingHologram(id));
  }

  @Override
  public String getId() {
    return id;
  }

  public Vector3D getPosition() {
    return position;
  }

  public void setPosition(Vector3D position) {
    this.position = position;
  }

  public List<HologramLine> getLines() {
    return lines;
  }

  public RenderAuthorizer getRenderAuthorizer() {
    return renderAuthorizer;
  }

  public void setRenderAuthorizer(RenderAuthorizer renderAuthorizer) {
    this.renderAuthorizer = renderAuthorizer;
  }

  public int getRenderDistance() {
    return renderDistance;
  }

  public void setRenderDistance(int renderDistance) {
    this.renderDistance = renderDistance;
  }

  @Override
  public Iterator<HologramLine> iterator() {
    return lines.iterator();
  }

  public List<String> getViewers() {
    return viewers;
  }

  public void addViewer(Player player) {
    viewers.add(player.getUniqueId().toString());
  }

  public void removeViewer(Player player) {
    viewers.remove(player.getUniqueId().toString());
  }

  public boolean canView(Player player) {
    return viewers.contains(player.getUniqueId().toString());
  }

  public Vector3D nextPosition(HologramLine line) {
    double y = lines.indexOf(line) * HologramLine.CUSTOM_NAME_VERTICAL_OFFSET;

    return new Vector3D(
      position.getWorld(),
      position.getX(),
      position.getY() - y,
      position.getZ()
    );
  }

  public void addLine(HologramLine line) {
    line.setPosition(nextPosition(line));
    lines.add(line);
  }

  public void replaceLine(int index,
                          HologramLine line) {
    HologramLine currentLine = lines.get(index);

    if (currentLine == null) {
      return;
    }

    line.setPosition(currentLine.getPosition());
    lines.set(index, line);
  }

  public void removeLine(int index) {
    lines.remove(index);

    for (HologramLine line : lines) {
      Vector3D nextLinePosition = nextPosition(line);
      line.setPosition(nextLinePosition);
    }
  }

  public static class Builder {
    private String id;
    private Vector3D position;
    private List<HologramLine> lines;
    private RenderAuthorizer renderAuthorizer;
    private int renderDistance;

    public Vector3D nextPosition() {
      double y = lines.size() * HologramLine.CUSTOM_NAME_VERTICAL_OFFSET;

      return new Vector3D(
        position.getWorld(),
        position.getX(),
        position.getY() - y,
        position.getZ()
      );
    }

    public Builder id(String id) {
      this.id = id;

      return this;
    }

    public Builder position(Vector3D position) {
      this.position = position;

      return this;
    }

    public Builder lines(List<HologramLine> lines) {
      this.lines = new ArrayList<>();

      for (HologramLine line : lines) {
        line.setPosition(nextPosition());
        this.lines.add(line);
      }

      return this;
    }

    public Builder addLine(HologramLine line) {
      if (lines == null) {
        lines = new ArrayList<>();
      }

      line.setPosition(nextPosition());

      lines.add(line);

      return this;
    }

    public Builder addLines(List<HologramLine> lines) {
      if (this.lines == null) {
        this.lines = new ArrayList<>();
      }

      for (HologramLine line : lines) {
        line.setPosition(nextPosition());
        this.lines.add(line);
      }

      return this;
    }

    public Builder addLines(HologramLine... lines) {
      return addLines(Arrays.asList(lines));
    }

    public Builder renderAuthorizer(RenderAuthorizer renderAuthorizer) {
      this.renderAuthorizer = renderAuthorizer;

      return this;
    }

    public Builder renderDistance(int renderDistance) {
      this.renderDistance = renderDistance;

      return this;
    }

    public Hologram build() {
      checkNotNull(id, "id");
      checkNotNull(position, "position");
      checkNotNull(lines, "lines");

      if (renderAuthorizer == null) {
        renderAuthorizer = RenderAuthorizer.empty();
      }

      if (renderDistance == 0) {
        renderDistance = 8;
      }

      return new Hologram(
        id, position, lines,
        renderAuthorizer,
        renderDistance
      );
    }
  }
}
