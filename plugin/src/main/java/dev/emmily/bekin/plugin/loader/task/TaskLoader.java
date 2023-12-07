package dev.emmily.bekin.plugin.loader.task;

import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.loader.Loader;
import dev.emmily.bekin.plugin.task.HologramLineUpdater;

import javax.inject.Inject;

public class TaskLoader
  implements Loader {
  private final BekinPlugin plugin;
  private final HologramLineUpdater hologramLineUpdater;
  private int taskId;

  @Inject
  public TaskLoader(BekinPlugin plugin,
                    HologramLineUpdater hologramLineUpdater) {
    this.plugin = plugin;
    this.hologramLineUpdater = hologramLineUpdater;
  }

  @Override
  public void onLoad() {
    taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
      plugin,
      hologramLineUpdater,
      0,
      20
    );
  }

  @Override
  public void onUnload() {
    plugin.getServer().getScheduler().cancelTask(taskId);
  }
}
