package dev.emmily.bekin.plugin.error;

import dev.emmily.bekin.plugin.BekinPlugin;

import javax.inject.Inject;

public class ErrorNotifier {
  private final BekinPlugin plugin;

  @Inject
  public ErrorNotifier(BekinPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Prints the stacktrace of the given
   * {@code error} in Bukkit's main
   * thread.
   *
   * @param error The error to be printed.
   */
  public void notify(Throwable error) {
    if (error == null) {
      return;
    }

    plugin.getServer().getScheduler().runTask(
      plugin,
      error::printStackTrace
    );
  }
}
