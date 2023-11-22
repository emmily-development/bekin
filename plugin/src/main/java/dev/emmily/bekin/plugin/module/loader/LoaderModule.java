package dev.emmily.bekin.plugin.module.loader;

import dev.emmily.bekin.plugin.loader.Loader;
import dev.emmily.bekin.plugin.loader.command.CommandLoader;
import dev.emmily.bekin.plugin.loader.hologram.HologramLoader;
import dev.emmily.bekin.plugin.loader.listener.ListenerLoader;
import dev.emmily.bekin.plugin.loader.message.PaginatedMessageLoader;
import dev.emmily.bekin.plugin.loader.service.BukkitServiceLoader;
import me.yushust.inject.AbstractModule;

public class LoaderModule
  extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Loader.class)
      .asSet()
      .to(HologramLoader.class)
      .to(CommandLoader.class)
      .to(ListenerLoader.class)
      .to(PaginatedMessageLoader.class)
      .to(BukkitServiceLoader.class);
  }
}
