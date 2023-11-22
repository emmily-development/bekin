package dev.emmily.bekin.api.util;

import com.cryptomorin.xseries.XSound;
import org.bukkit.entity.Player;

public interface SoundPlayer {
  static void play(Player player,
                   XSound sound,
                   float volume,
                   float pitch) {
    player.playSound(player.getLocation(), sound.parseSound(), volume, pitch);
  }
}
