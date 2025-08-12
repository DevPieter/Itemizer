package nl.devpieter.itemizer.runners;

import nl.devpieter.itemizer.managers.ItemManager;
import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.item.modifiers.IDayNightCycle;
import nl.devpieter.itemizer.models.CustomItemHolder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DayNightCycleRunner implements Runnable {

    private final ItemManager manager = ItemManager.getInstance();
    private final HashMap<UUID, Boolean> nightStatus = new HashMap<>();

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            boolean isNight = world.getTime() >= 13000 && world.getTime() <= 23000;

            List<CustomItemHolder> holders = this.getAllSpecialInventoryItems(world);
            if (holders.isEmpty()) continue;

            for (CustomItemHolder holder : holders) {
                if (!(holder.customItem() instanceof IDayNightCycle dayNightCycle)) continue;
                dayNightCycle.onTickDayNightCycle(holder.itemStack(), holder.player(), world.getTime(), isNight);
            }

            if (this.nightStatus.getOrDefault(world.getUID(), false) == isNight) continue;
            this.nightStatus.put(world.getUID(), isNight);

            for (CustomItemHolder holder : holders) {
                if (!(holder.customItem() instanceof IDayNightCycle dayNightCycle)) continue;
                ItemStack itemStack = holder.itemStack();

                if (isNight) dayNightCycle.onBecomeNight(itemStack, holder.player(), world.getTime());
                else dayNightCycle.onBecomeDay(itemStack, holder.player(), world.getTime());
            }
        }
    }

    private List<CustomItemHolder> getAllSpecialInventoryItems(World world) {
        List<CustomItemHolder> holders = new ArrayList<>();

        for (Player player : world.getPlayers()) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null || item.isEmpty() || item.getType().isAir()) continue;

                ICustomItem specialItem = this.manager.findSpecialItem(item);
                if (!(specialItem instanceof IDayNightCycle)) continue;

                holders.add(new CustomItemHolder(specialItem, item, player));
            }

            ItemStack cursorStack = player.getOpenInventory().getCursor();
            if (!cursorStack.isEmpty() && !cursorStack.getType().isAir()) {
                ICustomItem specialItem = this.manager.findSpecialItem(cursorStack);
                if (!(specialItem instanceof IDayNightCycle)) continue;

                holders.add(new CustomItemHolder(specialItem, cursorStack, player));
            }
        }

        return holders;
    }
}
