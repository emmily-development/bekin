package dev.emmily.bekin.protocol.v1_20_R2.protocol;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface MinecraftProtocol {
  DataWatcherObject<Optional<IChatBaseComponent>> DATA_CUSTOM_NAME = DataWatcher.a(Entity.class, DataWatcherRegistry.g);

  static WorldServer getWorld(World bukkitWorld) {
    return ((CraftWorld) bukkitWorld).getHandle();
  }

  static WorldServer getWorld(String world) {
    return getWorld(Bukkit.getWorld(world));
  }

  static PlayerConnection getConnection(Player player) {
    return ((CraftPlayer) player).getHandle().c;
  }

  static void sendPackets(Player player,
                          Packet<?>... packets) {
    PlayerConnection connection = getConnection(player);

    for (Packet<?> packet : packets) {
      connection.b(packet);
    }
  }
}
