package nl.devpieter.itemizer.block;

import net.kyori.adventure.text.Component;
import nl.devpieter.itemizer.item.CustomItem;
import nl.devpieter.utilize.common.utils.TextUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class DecayableBlock extends CustomItem implements IDecayableBlock {

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = super.getItemStack();
        List<Component> newLore = new ArrayList<>();

        List<Component> currentLore = itemStack.lore();
        if (currentLore != null) newLore.addAll(currentLore);

        newLore.add(TextUtils.miniText("<i><gray>This block will decay in <b>" + this.getFormattedDecayTime() + "</b>."));
        itemStack.editMeta(itemMeta -> itemMeta.lore(newLore));
        return itemStack;
    }

    protected String getFormattedDecayTime() {
        int seconds = this.getDecayTime() / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();
        if (minutes > 0) formattedTime.append(minutes).append(" minute").append(minutes > 1 ? "s" : "");

        if (seconds > 0) {
            if (!formattedTime.isEmpty()) formattedTime.append(" and");
            formattedTime.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        }

        if (formattedTime.isEmpty()) formattedTime.append("0 seconds");
        return formattedTime.toString();
    }
}
