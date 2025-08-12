package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public interface IDamagable extends ICustomItem {

    default void onPlayerItemDamage(ItemStack itemStack, PlayerItemDamageEvent event) {
    }
}
