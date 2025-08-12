package nl.devpieter.itemizer.listeners;

import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.item.modifiers.IShootable;
import nl.devpieter.itemizer.managers.ItemManager;
import nl.devpieter.itemizer.models.CustomProjectileHolder;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShootableItemListener implements Listener {

    private final ItemManager manager = ItemManager.getInstance();
    private final List<CustomProjectileHolder> projectileList = new ArrayList<>();

    @EventHandler
    private void onEntityShootBow(EntityShootBowEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getProjectile() instanceof AbstractArrow projectile)) return;

        ItemStack itemStack = event.getBow();

        ICustomItem specialItem = this.manager.findSpecialItem(itemStack);
        if (!(specialItem instanceof IShootable shootable)) return;

        CustomProjectileHolder holder = new CustomProjectileHolder(specialItem, itemStack, event.getEntity(), projectile);
        this.projectileList.add(holder);

        shootable.onEntityShootBow(holder, event);
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        if (event.isCancelled()) return;

        CustomProjectileHolder holder = this.findSpecialItem(event.getEntity());
        if (holder == null) return;

        ICustomItem specialItem = holder.customItem();
        if (!(specialItem instanceof IShootable shootable)) return;

        shootable.onProjectileHit(holder, event);
    }

    @EventHandler
    private void onEntityRemove(EntityRemoveEvent event) {
        CustomProjectileHolder holder = this.findSpecialItem(event.getEntity());
        if (holder == null) return;

        this.projectileList.remove(holder);

        ICustomItem specialItem = holder.customItem();
        if (!(specialItem instanceof IShootable shootable)) return;

        shootable.onProjectileRemove(holder, event);
    }

    private CustomProjectileHolder findSpecialItem(Entity projectile) {
        return this.projectileList.stream()
                .filter(holder -> holder.projectile().getUniqueId().equals(projectile.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    public void removeForgottenProjectiles() {
        this.projectileList.removeIf(holder -> !holder.projectile().isValid());
    }
}
