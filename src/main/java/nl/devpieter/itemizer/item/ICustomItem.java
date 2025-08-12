package nl.devpieter.itemizer.item;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICustomItem extends Listener {

    ItemStack getItemStack();

    boolean isThisItem(@Nullable ItemStack itemStack);

    NamespacedKey getNamespacedKey();

    void applyNamespacedKey(@NotNull ItemStack itemStack);
}
