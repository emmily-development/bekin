package dev.emmily.bekin.plugin.command.internal;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HologramPartFactory
  implements PartFactory {
  private final HologramRegistry hologramRegistry;

  @Inject
  public HologramPartFactory(HologramRegistry hologramRegistry) {
    this.hologramRegistry = hologramRegistry;
  }

  @Override
  public CommandPart createPart(String name,
                                List<? extends Annotation> modifiers) {
    return new HologramPart(name);
  }

  private class HologramPart
    implements ArgumentPart {
    private final String name;

    public HologramPart(String name) {
      this.name = name;
    }

    @Override
    public List<?> parseValue(CommandContext context,
                              ArgumentStack stack,
                              CommandPart caller) throws ArgumentParseException {
      String next = stack.hasNext() ? stack.next() : "";

      if (next.equals("")) {
        return Collections.emptyList();
      }

      return Collections.singletonList(hologramRegistry.get(next));
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext,
                                       ArgumentStack stack) {
      String next = stack.hasNext() ? stack.next() : "";

      List<String> result = new ArrayList<>();
      for (Hologram hologram : hologramRegistry.getAll()) {
        String id = hologram.getId();

        if (id.startsWith(next)) {
          result.add(id);
        }
      }

      return result;
    }

    @Override
    public String getName() {
      return name;
    }
  }
}
