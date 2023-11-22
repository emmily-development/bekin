package dev.emmily.bekin.plugin.module;

import com.google.gson.GsonBuilder;
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
import me.yushust.inject.Provides;
import me.yushust.inject.key.TypeReference;
import me.yushust.message.MessageHandler;

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
    bind(HologramRegistry.class).toInstance(new HologramRegistry(hologramRepository));

    bind(ErrorNotifier.class).singleton();

    bind(TypeReference.of(ModelRepository.class, PaginatedMessage.class)).toInstance(new MapModelRepository<>());

    install(
      new MessageModule(plugin),
      new ListenerModule(),
      new LoaderModule()
    );
  }

  @Provides
  public HologramHandler provideHologramHandler(MessageHandler messageHandler) {
    return HologramHandler.getInstance(messageHandler);
  }
}
