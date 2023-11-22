package dev.emmily.bekin.protocol.v1_8_R3.util;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface MinecraftProtocol {
  static WorldServer getWorld(World bukkitWorld) {
    return ((CraftWorld) bukkitWorld).getHandle();
  }

  static WorldServer getWorld(String world) {
    return getWorld(Bukkit.getWorld(world));
  }

  static PlayerConnection getConnection(Player bukkitPlayer) {
    return ((CraftPlayer) bukkitPlayer).getHandle().playerConnection;
  }

  static void sendPackets(Player bukkitPlayer,
                          Packet<?>... packets) {
    PlayerConnection connection = getConnection(bukkitPlayer);

    for (Packet<?> packet : packets) {
      connection.sendPacket(packet);
    }
  }
}
