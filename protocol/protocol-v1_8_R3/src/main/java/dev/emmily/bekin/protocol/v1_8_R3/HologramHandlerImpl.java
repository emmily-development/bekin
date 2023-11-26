package dev.emmily.bekin.protocol.v1_8_R3;

import dev.emmily.bekin.api.hologram.Hologram;
import dev.emmily.bekin.api.hologram.handler.HologramHandler;
import dev.emmily.bekin.api.hologram.line.HologramLine;
import dev.emmily.bekin.api.hologram.line.decorator.click.ClickableHologramLine;
import dev.emmily.bekin.api.hologram.spatial.PrTreeRegistry;
import dev.emmily.bekin.api.spatial.tree.CopyOnWritePRTree;
import dev.emmily.bekin.api.spatial.vectorial.BoundingBox;
import dev.emmily.bekin.api.spatial.vectorial.Vector3D;
import dev.emmily.bekin.api.util.lang.LanguageProvider;
import dev.emmily.bekin.protocol.v1_8_R3.protocol.MinecraftProtocol;
import me.yushust.message.MessageHandler;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HologramHandlerImpl
  implements HologramHandler {
  private final MessageHandler messageHandler;
  private final PrTreeRegistry prTreeRegistry;

  public HologramHandlerImpl(MessageHandler messageHandler,
                             PrTreeRegistry prTreeRegistry) {
    this.messageHandler = messageHandler;
    this.prTreeRegistry = prTreeRegistry;
  }


  @Override
  public void spawn(Hologram hologram) {
    WorldServer world = MinecraftProtocol.getWorld(hologram.getPosition().getWorld());

    for (HologramLine line : hologram) {
      Vector3D position = hologram.nextPosition(line);
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

      if (!(line instanceof ClickableHologramLine)) {
        // non-clickable lines don't require a hitbox
        armorStand.n(true);
      }

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

      if (entity instanceof EntityArmorStand) {
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

        if (entity instanceof EntityArmorStand) {
          EntityArmorStand armorStand = (EntityArmorStand) entity;
          DataWatcher dataWatcher = armorStand.getDataWatcher();

          String text = line.getContent().apply(player, messageHandler);
          dataWatcher.watch(2, text);

          MinecraftProtocol.sendPackets(
            player,
            new PacketPlayOutSpawnEntityLiving(armorStand),
            new PacketPlayOutEntityMetadata(armorStand.getId(), dataWatcher, true)
          );
          viewers.add(player.getUniqueId().toString());

          if (line instanceof ClickableHologramLine) {
            assignBoundingBox(hologram, entity.getBoundingBox(), line, player);
          }
        }
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

      if (entity instanceof EntityArmorStand) {
        EntityArmorStand armorStand = (EntityArmorStand) entity;
        MinecraftProtocol.sendPackets(player, new PacketPlayOutEntityDestroy(armorStand.getId()));
      }
    }

    viewers.remove(player.getUniqueId().toString());
  }

  @Override
  public void move(Hologram hologram,
                   Vector3D newPosition) {
    WorldServer world = MinecraftProtocol.getWorld(newPosition.getWorld());
    hologram.setPosition(newPosition);

    for (HologramLine line : hologram) {
      int id = line.getBackingEntityId();
      Entity entity = world.a(id);

      if (entity == null) {
        throw new IllegalArgumentException(String.format(
          "The armor stand of the line %s of the hologram %s couldn't be found. Did you use the kill command?",
          hologram.getLines().indexOf(line),
          hologram.getId()
        ));
      }

      Vector3D position = hologram.nextPosition(line);
      entity.setPosition(position.getX(), position.getY(), position.getZ());
      line.setPosition(position);
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      if (!hologram.getRenderAuthorizer().test(player)) {
        continue;
      }

      for (HologramLine line : hologram) {
        if (!(line instanceof ClickableHologramLine)) {
          continue;
        }

        int id = line.getBackingEntityId();
        Entity entity = world.a(id);
        assignBoundingBox(hologram, entity.getBoundingBox(), line, player);
      }

      hide(hologram, player);
      render(hologram, player);
    }
  }

  private void assignBoundingBox(Hologram hologram,
                                 AxisAlignedBB entityBoundingBox,
                                 HologramLine line,
                                 Player player) {
    ClickableHologramLine clickableLine = (ClickableHologramLine) line;
    double highestX = entityBoundingBox.d;
    double highestY = entityBoundingBox.e / 2;
    double highestZ = entityBoundingBox.f;

    Vector3D highestHitBoxPoint = new Vector3D(
      hologram.getPosition().getWorld(),
      highestX,
      highestY,
      highestZ
    );

    String text = line.getContent().apply(player, messageHandler);
    BoundingBox textBoundingBox = BoundingBox.forEntityCustomName(highestHitBoxPoint, text);
    clickableLine.registerBoundingBox(player, textBoundingBox);

    String language = LanguageProvider.locale().getLanguage(player);
    CopyOnWritePRTree<ClickableHologramLine> prTree = prTreeRegistry.getOrCreate(language);

    prTree.add(clickableLine);
  }
}
