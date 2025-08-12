package nl.devpieter.itemizer.item.modifiers;

import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.models.CustomProjectileHolder;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface IShootable extends ICustomItem {

    default void onEntityShootBow(CustomProjectileHolder holder, EntityShootBowEvent event) {
    }

    default void onProjectileHit(CustomProjectileHolder holder, ProjectileHitEvent event) {
    }

    default void onProjectileRemove(CustomProjectileHolder holder, EntityRemoveEvent event) {
    }
}
