package nl.devpieter.itemizer.models;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class CustomProjectileHolder {

    private final ICustomItem customItem;
    private final ItemStack itemStack;
    private final LivingEntity shooter;
    private AbstractArrow projectile;

    public CustomProjectileHolder(ICustomItem customItem, ItemStack itemStack, LivingEntity shooter, AbstractArrow projectile) {
        this.customItem = customItem;
        this.itemStack = itemStack;
        this.shooter = shooter;
        this.projectile = projectile;
    }

    public ICustomItem customItem() {
        return this.customItem;
    }

    public ItemStack itemStack() {
        return this.itemStack;
    }

    public LivingEntity shooter() {
        return this.shooter;
    }

    public AbstractArrow projectile() {
        return this.projectile;
    }

    public void setProjectile(AbstractArrow projectile) {
        this.projectile = projectile;
    }
}