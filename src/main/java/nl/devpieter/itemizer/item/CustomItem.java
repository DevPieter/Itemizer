package nl.devpieter.itemizer.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DeathProtection;
import net.kyori.adventure.text.Component;
import nl.devpieter.itemizer.Itemizer;
import nl.devpieter.itemizer.item.modifiers.IResurrectable;
import nl.devpieter.utilize.common.utils.TextUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CustomItem implements ICustomItem {

    protected abstract @NotNull String getUniqueId();

    protected abstract @NotNull Component getDisplayName();

    protected abstract @NotNull List<Component> getLore();

    protected abstract @NotNull ItemStack getSpecialItem();

    protected @Nullable String getModelName() {
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = this.getSpecialItem();

        itemStack.editMeta(meta -> {
            meta.displayName(this.getDisplayName());
            meta.lore(this.getLore());
        });

        if (this.getModelName() != null) {
            CustomModelData.Builder modelData = CustomModelData.customModelData().addString(this.getModelName());
            itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, modelData.build());
        }

        if (this instanceof IResurrectable resurrectable) {
            DeathProtection deathProtection = DeathProtection.deathProtection(resurrectable.getDeathEffects());
            itemStack.setData(DataComponentTypes.DEATH_PROTECTION, deathProtection);
        }

        this.applyNamespacedKey(itemStack);
        return itemStack;
    }

    @Override
    public boolean isThisItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) return false;
        if (!itemStack.hasItemMeta()) return false;

        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(this.getNamespacedKey(), PersistentDataType.BOOLEAN);
    }

    @Override
    public NamespacedKey getNamespacedKey() {
        return new NamespacedKey(Itemizer.getInstance(), this.getUniqueId());
    }

    @Override
    public void applyNamespacedKey(@NotNull ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (container.has(this.getNamespacedKey(), PersistentDataType.BOOLEAN)) return;

        itemStack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(this.getNamespacedKey(), PersistentDataType.BOOLEAN, true);
        });
    }

    public void sendItemMessage(@NotNull Player player, @NotNull String miniMessage) {
        this.sendItemMessage(player, TextUtils.miniText(miniMessage));
    }

    public void sendItemMessage(@NotNull Player player, @NotNull Component message) {
        player.sendMessage(this.getDisplayName().append(Component.space()).append(message));
    }
}
