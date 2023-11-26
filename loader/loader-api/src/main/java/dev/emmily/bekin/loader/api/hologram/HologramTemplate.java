package dev.emmily.bekin.loader.api.hologram;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.bekin.loader.api.hologram.line.HologramLineTemplate;
import dev.emmily.bekin.loader.api.hologram.render.RenderAuthorizerTemplate;
import dev.emmily.sigma.api.Model;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * Represents a {@link Hologram} within a configuration file.
 */
public class HologramTemplate
  implements Model {
  private final String id;
  private final Vector3D position;
  private final List<HologramLineTemplate> lines;
  private final RenderAuthorizerTemplate renderAuthorizer;
  private final int renderDistance;

  @ConstructorProperties({
    "id", "position", "lines",
    "renderAuthorizer",
    "renderDistance"
  })
  public HologramTemplate(String id,
                          Vector3D position,
                          List<HologramLineTemplate> lines,
                          RenderAuthorizerTemplate renderAuthorizer,
                          int renderDistance) {
    this.id = id;
    this.position = position;
    this.lines = lines;
    this.renderAuthorizer = renderAuthorizer;
    this.renderDistance = renderDistance;
  }

  @Override
  public String getId() {
    return id;
  }

  public Vector3D getPosition() {
    return position;
  }

  public List<HologramLineTemplate> getLines() {
    return lines;
  }

  public RenderAuthorizerTemplate getRenderAuthorizer() {
    return renderAuthorizer;
  }

  public int getRenderDistance() {
    return renderDistance;
  }
}
