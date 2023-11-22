package dev.emmily.bekin.plugin.module.message;

import com.cryptomorin.xseries.XSound;
import dev.emmily.bekin.plugin.BekinPlugin;
import dev.emmily.bekin.plugin.message.MessageMode;
import dev.emmily.bekin.api.util.SoundPlayer;
import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;
import me.yushust.message.MessageHandler;
import me.yushust.message.bukkit.BukkitMessageAdapt;
import me.yushust.message.bukkit.SpigotLinguist;
import me.yushust.message.language.Linguist;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class MessageModule
  extends AbstractModule {
  private final BekinPlugin plugin;

  public MessageModule(BekinPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    Linguist<Player> playerLinguist = new SpigotLinguist();
    bind(TypeReference.of(Linguist.class, Player.class)).toInstance(playerLinguist);
    bind(MessageHandler.class).toInstance(MessageHandler.of(
      BukkitMessageAdapt.newYamlSource(plugin, new File(plugin.getDataFolder() + "/lang/")),
      configurationHandle -> {
        configurationHandle
          .specify(CommandSender.class)
          .setLinguist(commandSender -> "es")
          .setMessageSender((sender, mode, message) -> sender.sendMessage(message));
        configurationHandle
          .specify(Player.class)
          .setLinguist(playerLinguist)
          .setMessageSender((player, mode, message) -> {
            Runnable action;

            switch (mode) {
              case MessageMode.SUCCESS: {
                action = () -> SoundPlayer.play(
                  player,
                  XSound.ENTITY_PLAYER_LEVELUP,
                  1,
                  1
                );

                break;
              }
              case MessageMode.ERROR: {
                action = () -> SoundPlayer.play(
                  player,
                  XSound.BLOCK_NOTE_BLOCK_BASS,
                  1,
                  1
                );

                break;
              }
              default: {
                action = () -> SoundPlayer.play(
                  player,
                  XSound.UI_BUTTON_CLICK,
                  1,
                  1
                );

                break;
              }
            }

            player.sendMessage(message);
            action.run();
          });
        configurationHandle.addInterceptor(s -> ChatColor.translateAlternateColorCodes('&', s));
      }
    ));
  }
}
