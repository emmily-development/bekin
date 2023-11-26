package dev.emmily.bekin.loader.api.hologram.line.decorator;

import dev.emmily.bekin.loader.api.hologram.line.HologramLineTemplate;
import dev.emmily.bekin.loader.api.hologram.line.provider.TextProviderTemplate;

import java.util.List;

public abstract class AbstractLineTemplateDecorator
  implements HologramLineTemplate {
  private final HologramLineTemplate wrapped;

  public AbstractLineTemplateDecorator(HologramLineTemplate wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public List<Type> getType() {
    return wrapped.getType();
  }

  @Override
  public TextProviderTemplate getTextProvider() {
    return wrapped.getTextProvider();
  }
}
