package nl.devpieter.itemizer.models;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public record CustomItemHolder(ICustomItem customItem, ItemStack itemStack, Player player) {
}
