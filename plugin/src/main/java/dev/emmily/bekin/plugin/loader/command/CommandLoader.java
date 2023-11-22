package dev.emmily.bekin.plugin.loader.command;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.command.HologramCommand;
import dev.emmily.bekin.plugin.command.internal.HologramPartFactory;
import dev.emmily.bekin.plugin.loader.Loader;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilder;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.command.Command;
import me.yushust.inject.Injector;

import javax.inject.Inject;
import java.util.List;

public class CommandLoader
  implements Loader {
  private final HologramCommand hologramCommand;
  private final HologramPartFactory hologramPartFactory;
  private final Injector injector;
  private final BekinPlugin plugin;

  @Inject
  public CommandLoader(HologramCommand hologramCommand,
                       HologramPartFactory hologramPartFactory,
                       Injector injector,
                       BekinPlugin plugin) {
    this.hologramCommand = hologramCommand;
    this.hologramPartFactory = hologramPartFactory;
    this.injector = injector;
    this.plugin = plugin;
  }

  @Override
  public void onLoad() {
    plugin.getLogger().info("Loading commands...");

    PartInjector partInjector = PartInjector.create();
    partInjector.install(new DefaultsModule());
    partInjector.install(new BukkitModule());
    partInjector.install(new AbstractModule() {
      @Override
      public void configure() {
        bindFactory(Hologram.class, hologramPartFactory);
      }
    });

    CommandManager commandManager = new BukkitCommandManager("bekin");
    AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(
      AnnotatedCommandBuilder.create(partInjector),
      (clazz, parent) -> injector.getInstance(clazz)
    );

    List<Command> commands = treeBuilder.fromClass(hologramCommand);
    commandManager.registerCommands(commands);

    plugin.getLogger().info("Successfully loaded " + commands.size() + " commands");
  }

  @Override
  public void onUnload() {

  }
}
