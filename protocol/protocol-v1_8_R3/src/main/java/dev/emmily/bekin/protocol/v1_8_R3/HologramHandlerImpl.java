package dev.emmily.bekin.protocol.v1_8_R3;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import dev.emmily.bekin.protocol.v1_8_R3.protocol.MinecraftProtocol;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static dev.emmily.bekin.api.hologram.handler.HologramHandler.unknownEntity;

public class HologramHandlerImpl
  implements HologramHandler {
  @Override
  public void spawn(Hologram hologram) {
    WorldServer world = MinecraftProtocol.getWorld(hologram.getPosition().getWorld());

    for (HologramLine line : hologram) {
      Position position = hologram.nextPosition(line);
      EntityArmorStand armorStand = new EntityArmorStand(
        world,
        position.getX(),
        position.getY(),
        position.getZ()
      );
      armorStand.setSmall(true);
      armorStand.setArms(false);
      armorStand.setGravity(true);
      armorStand.setInvisible(true);
      armorStand.setCustomNameVisible(true);
      armorStand.n(true); // marker

      world.addEntity(armorStand);
      line.setBackingEntityId(armorStand.getId());
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
            new PacketPlayOutEntityDestroy(line.getBackingEntityId())
          );
        }
      }

      viewers.clear();
      world.removeEntity(entity);
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

        EntityArmorStand armorStand = (EntityArmorStand) entity;
        DataWatcher dataWatcher = armorStand.getDataWatcher();
        dataWatcher.watch(2, line.getContent().apply(player));

        MinecraftProtocol.sendPackets(
          player,
          new PacketPlayOutSpawnEntityLiving(armorStand),
          new PacketPlayOutEntityMetadata(armorStand.getId(), dataWatcher, true)
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
        new PacketPlayOutEntityDestroy(entity.getId())
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
      entity.setPosition(position.getX(), position.getY(), position.getZ());
      line.setPosition(position);
    }
  }
}
