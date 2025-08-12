package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface IInteractable extends ICustomItem {

    default void onPlayerInteract(ItemStack itemStack, PlayerInteractEvent event) {
    }

    default void onPlayerInteractEntity(ItemStack itemStack, PlayerInteractEntityEvent event) {
    }
}
