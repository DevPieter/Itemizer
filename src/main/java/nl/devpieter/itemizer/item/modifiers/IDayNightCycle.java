package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IDayNightCycle extends ICustomItem {

    default void onTickDayNightCycle(ItemStack itemStack, Player player, long time, boolean isNight) {
    }

    default void onBecomeDay(ItemStack itemStack, Player player, long time) {
    }

    default void onBecomeNight(ItemStack itemStack, Player player, long time) {
    }
}
