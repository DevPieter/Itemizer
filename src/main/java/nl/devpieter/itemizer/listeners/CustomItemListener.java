package nl.devpieter.itemizer.listeners;

import nl.devpieter.itemizer.managers.ItemManager;
import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.item.modifiers.IAttackable;
import nl.devpieter.itemizer.item.modifiers.IDamagable;
import nl.devpieter.itemizer.item.modifiers.IDroppable;
import nl.devpieter.itemizer.item.modifiers.IInteractable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CustomItemListener implements Listener {

    private final ItemManager manager = ItemManager.getInstance();

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getDamager() instanceof LivingEntity livingDamager)) return;

        EntityEquipment equipment = livingDamager.getEquipment();
        if (equipment == null) return;

        ItemStack itemStack = equipment.getItemInMainHand();
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IAttackable attackable)) return;
        attackable.onEntityDamageByEntity(itemStack, event);
    }

    @EventHandler
    private void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (event.isCancelled()) return;

        ItemStack itemStack = event.getItem();
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IDamagable damageable)) return;
        damageable.onPlayerItemDamage(itemStack, event);
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;

        ItemStack itemStack = event.getItemDrop().getItemStack();
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IDroppable droppable)) return;
        droppable.onPlayerDropItem(itemStack, event);
    }

    @EventHandler
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.isCancelled()) return;

        ItemStack itemStack = event.getItem().getItemStack();
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IDroppable droppable)) return;
        droppable.onEntityPickupItem(itemStack, event);
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IInteractable interactable)) return;
        interactable.onPlayerInteract(itemStack, event);
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) return;

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getHand());
        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);

        if (!(specialItem instanceof IInteractable interactable)) return;
        interactable.onPlayerInteractEntity(itemStack, event);
    }
}
