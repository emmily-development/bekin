package dev.emmily.bekin.api.hologram.line.decorator.click.action.sound;

import com.cryptomorin.xseries.XSound;
import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import dev.emmily.bekin.api.util.SoundPlayer;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;

public class PlaySoundClickAction
  implements HologramClickAction {
  private final XSound sound;
  private final float volume;
  private final float pitch;

  @ConstructorProperties({
    "sound", "volume", "pitch"
  })
  public PlaySoundClickAction(XSound sound,
                              float volume,
                              float pitch) {
    this.sound = sound;
    this.volume = volume;
    this.pitch = pitch;
  }

  @Override
  public void accept(Player player) {
    SoundPlayer.play(player, sound, volume, pitch);
  }
}
