package nl.devpieter.itemizer.listeners;

import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.item.modifiers.IResurrectable;
import nl.devpieter.itemizer.managers.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ResurrectableItemListener implements Listener {

    private final ItemManager manager = ItemManager.getInstance();

    @EventHandler
    private void onEntityResurrect(EntityResurrectEvent event) {
//        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        EquipmentSlot slot = event.getHand();
        if (slot == null) return;

        ItemStack itemStack = player.getEquipment().getItem(slot);
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IResurrectable resurrectable)) return;
        resurrectable.onPlayerResurrect(itemStack, event);
    }
}
