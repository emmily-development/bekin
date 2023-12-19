package dev.emmily.bekin.protocol.v1_20_R2;

import com.mojang.math.Transformation;
import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import dev.emmily.bekin.extensions.textdisplay.TextDisplayHologramLine;
import dev.emmily.bekin.protocol.v1_20_R2.protocol.MinecraftProtocol;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.emmily.bekin.api.hologram.handler.HologramHandler.unknownEntity;

public class TextDisplayHologramHandler
  implements HologramHandler {
  @Override
  public void spawn(Hologram hologram) {
    WorldServer world = MinecraftProtocol.getWorld(hologram.getPosition().getWorld());

    List<HologramLine> lines = hologram.getLines();
    for (int i = 0; i < lines.size(); i++) {
      TextDisplayHologramLine line = (TextDisplayHologramLine) lines.get(i);
      Position position;

      if (i > 0) {
        position = hologram.nextPosition(line, ((TextDisplayHologramLine) lines.get(i - 1)).getDisplayHeight());
      } else {
        position = hologram.nextPosition(line, 0);
      }

      Display.TextDisplay textDisplay = EntityTypes.aX.a(world);
      textDisplay.e(
        position.getX(),
        position.getY(),
        position.getZ()
      ); // set position
      DataWatcher dataWatcher = textDisplay.al();

      dataWatcher.b(Display.TextDisplay.aN, line.getLineWidth()); // set line width
      dataWatcher.b(Display.TextDisplay.aO, line.getBackgroundColor().asARGB()); // set background color
      textDisplay.c(line.getTextOpacity()); // set text opacity
      setFlag(textDisplay, 1, line.isShadowed()); // set shadowed
      setFlag(textDisplay, 2, line.isSeeThrough()); // set see through
      setFlag(textDisplay, 3, line.isDefaultBackground()); // set default background

      align(textDisplay, line.getTextAlignment());

      org.bukkit.util.Transformation transformation = line.getTransformation();
      textDisplay.a(new Transformation(
        transformation.getTranslation(),
        transformation.getLeftRotation(),
        transformation.getScale(),
        transformation.getRightRotation()
      )); // set transformation

      textDisplay.b(line.getInterpolationDuration()); // set interpolation duration
      textDisplay.c(line.getInterpolationDelay()); // set interpolation delay

      textDisplay.c(line.getShadowRadius()); // set shadow radius
      textDisplay.u(line.getShadowStrength()); // set shadow strength

      textDisplay.w(line.getDisplayHeight()); // set display height

      textDisplay.a(Display.BillboardConstraints.valueOf(line.getBillboard().name())); // set billboard
      textDisplay.m(line.getGlowColorOverride().asARGB()); // set glow color override

      org.bukkit.entity.Display.Brightness brightness = line.getBrightness();
      textDisplay.a(brightness == null
        ? null
        : new Brightness(brightness.getBlockLight(), brightness.getSkyLight())
      ); // set brightness

      world.b(textDisplay); // add entity
      line.setBackingEntityId(textDisplay.ah());
    }
  }

  @Override
  public void destroy(Hologram hologram) {
    WorldServer world = MinecraftProtocol.getWorld(hologram.getPosition().getWorld());
    List<String> viewers = hologram.getViewers();

    for (HologramLine line : hologram) {
      Entity entity = world.a(line.getBackingEntityId());

      if (entity == null) {
        throw unknownEntity(hologram, line);
      }

      for (String id : viewers) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(id));

        if (offlinePlayer.isOnline()) {
          MinecraftProtocol.sendPackets(
            offlinePlayer.getPlayer(),
            new PacketPlayOutEntityDestroy(entity.ah())
          );
        }
      }

      viewers.clear();
      entity.ak(); // de-spawn the entity
    }
  }

  @Override
  public void render(Hologram hologram,
                     Player player) {
    if (!hologram.getRenderAuthorizer().test(player)) {
      return;
    }

    WorldServer world = MinecraftProtocol.getWorld(hologram.getPosition().getWorld());
    List<String> viewers = hologram.getViewers();

    if (!viewers.contains(player.getUniqueId().toString())) {
      for (HologramLine line : hologram) {
        Entity entity = world.a(line.getBackingEntityId());

        if (entity == null) {
          throw unknownEntity(hologram, line);
        }

        Display.TextDisplay textDisplay = (Display.TextDisplay) entity;
        DataWatcher dataWatcher = textDisplay.al();
        dataWatcher.a(
          MinecraftProtocol.DATA_CUSTOM_NAME,
          Optional.ofNullable(CraftChatMessage.fromStringOrNull(line.getContent().apply(player)))
        ); // set custom name

        MinecraftProtocol.sendPackets(
          player,
          new PacketPlayOutSpawnEntity(textDisplay),
          new PacketPlayOutEntityMetadata(textDisplay.ah(), dataWatcher.b())
        );
        viewers.add(player.getUniqueId().toString());
      }
    }
  }

  @Override
  public void hide(Hologram hologram,
                   Player player) {
    WorldServer world = MinecraftProtocol.getWorld(hologram.getPosition().getWorld());
    List<String> viewers = hologram.getViewers();

    for (HologramLine line : hologram) {
      Entity entity = world.a(line.getBackingEntityId());

      if (entity == null) {
        throw unknownEntity(hologram, line);
      }

      MinecraftProtocol.sendPackets(
        player,
        new PacketPlayOutEntityDestroy(entity.ah())
      );
    }

    viewers.remove(player.getUniqueId().toString());
  }

  @Override
  public void move(Hologram hologram,
                   Position newPosition) {
    WorldServer world = MinecraftProtocol.getWorld(newPosition.getWorld());
    hologram.setPosition(newPosition);

    for (HologramLine line : hologram) {
      int id = line.getBackingEntityId();
      Entity entity = world.a(id);

      if (entity == null) {
        throw unknownEntity(hologram, line);
      }

      Position position = hologram.nextPosition(line);
      entity.p(position.getX(), position.getY(), position.getZ()); // set position
      line.setPosition(position);
    }
  }

  private void setFlag(Display.TextDisplay textDisplay,
                       int flag,
                       boolean value) {
    byte flags = textDisplay.y(); // get flags

    if (value) {
      flags = (byte) (flags | flag);
    } else {
      flags = (byte) (flags & ~flag);
    }

    textDisplay.d(flags);
  }

  private void align(Display.TextDisplay textDisplay,
                     TextDisplay.TextAlignment alignment) {
    switch (alignment) {
      case CENTER:
        this.setFlag(textDisplay, 8, false);
        this.setFlag(textDisplay, 16, false);

        break;
      case LEFT:
        this.setFlag(textDisplay, 8, true);
        this.setFlag(textDisplay, 16, false);

        break;
      case RIGHT:
        this.setFlag(textDisplay, 8, false);
        this.setFlag(textDisplay, 16, true);

        break;
      default:
        throw new IllegalArgumentException("Unknown alignment " + alignment);
    }
  }
}
