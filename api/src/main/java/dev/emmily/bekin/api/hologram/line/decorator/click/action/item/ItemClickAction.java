package dev.emmily.bekin.api.hologram.line.decorator.click.action.item;

import dev.emmily.bekin.api.hologram.line.decorator.click.action.HologramClickAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.beans.ConstructorProperties;

public class ItemClickAction
  implements HologramClickAction {
  private final ItemStack item;
  private final Operation operation;

  @ConstructorProperties({"item", "operation"})
  public ItemClickAction(ItemStack item,
                         Operation operation) {
    this.item = item;
    this.operation = operation;
  }

  @Override
  public void accept(Player player) {
    Inventory inventory = player.getInventory();

    if (operation == Operation.GIVE) {
      inventory.addItem(item);
    } else if (operation == Operation.REMOVE){
      inventory.removeItem(item);
    }
  }

  public enum Operation {
    GIVE,
    REMOVE
  }
}
