package nl.devpieter.itemizer.managers;

import nl.devpieter.itemizer.item.ICustomItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private static ItemManager INSTANCE;

    private final List<ICustomItem> specialItems = new ArrayList<>();

    private ItemManager() {
    }

    public void registerItem(@NotNull Plugin plugin, ICustomItem item) {
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.registerEvents(item, plugin);

        this.specialItems.add(item);
    }

    public static ItemManager getInstance() {
        if (INSTANCE == null) INSTANCE = new ItemManager();
        return INSTANCE;
    }

    public List<ICustomItem> getSpecialItems() {
        return this.specialItems;
    }

    public @Nullable ICustomItem findSpecialItem(ItemStack stack) {
        for (ICustomItem item : this.specialItems) {
            if (!item.isThisItem(stack)) continue;
            return item;
        }

        return null;
    }
}
