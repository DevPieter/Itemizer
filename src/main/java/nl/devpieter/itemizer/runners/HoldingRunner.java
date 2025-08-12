package nl.devpieter.itemizer.runners;

import nl.devpieter.itemizer.managers.ItemManager;
import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.item.modifiers.IHoldable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HoldingRunner implements Runnable {

    private final ItemManager manager = ItemManager.getInstance();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inventory = player.getInventory();

            IHoldable mainHandItem = this.getHoldableItem(inventory.getItemInMainHand());
            IHoldable offHandItem = this.getHoldableItem(inventory.getItemInOffHand());

            if (mainHandItem != null) {
                mainHandItem.onTickHoldable(inventory.getItemInMainHand(), player, EquipmentSlot.HAND);
            }

            if (offHandItem != null) {
                offHandItem.onTickHoldable(inventory.getItemInOffHand(), player, EquipmentSlot.OFF_HAND);
            }
        }
    }

    private IHoldable getHoldableItem(ItemStack item) {
        if (item.isEmpty() || item.getType().isAir()) return null;

        ICustomItem specialItem = this.manager.findSpecialItem(item);
        if (!(specialItem instanceof IHoldable holdable)) return null;

        return holdable;
    }
}
