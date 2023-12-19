package dev.emmily.bekin.protocol.v1_20_R2;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.spatial.vectorial.Position;
import dev.emmily.bekin.protocol.v1_20_R2.protocol.MinecraftProtocol;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.emmily.bekin.api.hologram.handler.HologramHandler.unknownEntity;

public class ArmorStandHologramHandler
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
      armorStand.t(true); // small
      armorStand.a(false); // arms
      armorStand.af = true; // gravity (disable physics)
      armorStand.j(true); // invisible
      armorStand.n(true); // custom name visible
      armorStand.u(true); // marker
      armorStand.s(true); // baseplate

      world.b(armorStand); // add entity
      line.setBackingEntityId(armorStand.ah());
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
          MinecraftProtocol.sendPackets(offlinePlayer.getPlayer(), new PacketPlayOutEntityDestroy(entity.ah()));
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

        EntityArmorStand armorStand = (EntityArmorStand) entity;
        DataWatcher dataWatcher = armorStand.al();
        dataWatcher.a(
          MinecraftProtocol.DATA_CUSTOM_NAME,
          Optional.ofNullable(CraftChatMessage.fromStringOrNull(line.getContent().apply(player)))
        ); // set custom name

        MinecraftProtocol.sendPackets(
          player,
          new PacketPlayOutSpawnEntity(armorStand),
          new PacketPlayOutEntityMetadata(armorStand.ah(), dataWatcher.b())
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
}
