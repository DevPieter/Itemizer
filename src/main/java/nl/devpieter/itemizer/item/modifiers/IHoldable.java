package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface IHoldable extends ICustomItem {

    default void onTickHoldable(ItemStack itemStack, Player player, EquipmentSlot slot) {
    }

    // Add onHolding, onRelease, ...
}
