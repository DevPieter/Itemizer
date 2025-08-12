package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public interface IAttackable extends ICustomItem {

    default void onEntityDamageByEntity(ItemStack itemStack, EntityDamageByEntityEvent event) {
    }
}
