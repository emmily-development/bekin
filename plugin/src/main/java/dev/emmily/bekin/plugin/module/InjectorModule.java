package dev.emmily.bekin.plugin.module;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.registry.HologramRegistry;
import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.error.ErrorNotifier;
import dev.emmily.bekin.plugin.message.PaginatedMessage;
import dev.emmily.bekin.plugin.module.listener.ListenerModule;
import dev.emmily.bekin.plugin.module.loader.LoaderModule;
import dev.emmily.bekin.plugin.module.message.MessageModule;
import dev.emmily.sigma.api.repository.CachedAsyncModelRepository;
import dev.emmily.sigma.api.repository.ModelRepository;
import dev.emmily.sigma.platform.codec.jackson.JacksonModelCodec;
import dev.emmily.sigma.platform.jdk.MapModelRepository;
import dev.emmily.sigma.platform.json.JsonModelRepository;
import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;

import java.io.File;

public class InjectorModule
  extends AbstractModule {
  private final BekinPlugin plugin;

  public InjectorModule(BekinPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(BekinPlugin.class).toInstance(plugin);

    CachedAsyncModelRepository<Hologram> hologramRepository = new JsonModelRepository<>(
      new MapModelRepository<>(),
      new JacksonModelCodec(),
      new File(plugin.getDataFolder(), "holograms"),
      Hologram.class
    );
    HologramRegistry hologramRegistry = new HologramRegistry(hologramRepository);
    bind(HologramRegistry.class).toInstance(hologramRegistry);

    bind(ErrorNotifier.class).singleton();

    bind(TypeReference.of(ModelRepository.class, PaginatedMessage.class)).toInstance(new MapModelRepository<>());

    bind(HologramHandler.class)
      .toInstance(HologramHandler.getInstance(HologramHandler.Namespace.ARMOR_STAND));
    bind(HologramHandler.class)
      .named("text-display")
      .toInstance(HologramHandler.getInstance(HologramHandler.Namespace.TEXT_DISPLAY));

    install(
      new MessageModule(plugin),
      new ListenerModule(hologramRegistry),
      new LoaderModule()
    );
  }
}
