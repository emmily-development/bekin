package dev.emmily.bekin.plugin;

import dev.emmily.bekin.plugin.loader.Loader;
import dev.emmily.bekin.plugin.module.InjectorModule;
import me.yushust.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Set;

public class BekinPlugin
  extends JavaPlugin {
  private Injector injector;

  @Inject private Set<Loader> loaders;

  @Override
  public void onEnable() {
    injector = Injector.create(new InjectorModule(this));
    injector.injectMembers(this);

    for (Loader loader : loaders) {
      loader.onLoad();
    }
  }

  @Override
  public void onDisable() {
    for (Loader loader : loaders) {
      loader.onUnload();
    }
  }
}
