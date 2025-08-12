package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public interface IDroppable extends ICustomItem {

    default void onPlayerDropItem(ItemStack itemStack, PlayerDropItemEvent event) {
    }

    default void onEntityPickupItem(ItemStack itemStack, EntityPickupItemEvent event) {
    }
}
