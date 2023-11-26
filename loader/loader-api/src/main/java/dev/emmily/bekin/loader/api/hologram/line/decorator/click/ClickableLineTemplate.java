package dev.emmily.bekin.loader.api.hologram.line.decorator.click;

import dev.emmily.bekin.loader.api.hologram.line.HologramLineTemplate;
import dev.emmily.bekin.loader.api.hologram.line.decorator.AbstractLineTemplateDecorator;
import dev.emmily.bekin.loader.api.hologram.line.decorator.click.action.HologramClickActionTemplate;

import java.beans.ConstructorProperties;

public class ClickableLineTemplate
  extends AbstractLineTemplateDecorator {
  private final HologramClickActionTemplate clickAction;

  @ConstructorProperties({
    "wrapped", "clickAction"
  })
  public ClickableLineTemplate(HologramLineTemplate wrapped,
                               HologramClickActionTemplate clickAction) {
    super(wrapped);
    this.clickAction = clickAction;
  }

  public HologramClickActionTemplate getClickAction() {
    return clickAction;
  }
}
