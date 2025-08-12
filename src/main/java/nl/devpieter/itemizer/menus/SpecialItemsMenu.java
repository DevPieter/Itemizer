package nl.devpieter.itemizer.menus;

import net.kyori.adventure.text.Component;
import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.managers.ItemManager;
import nl.devpieter.utilize.menus.enums.MenuSize;
import nl.devpieter.utilize.menus.menu.MenuBase;
import nl.devpieter.utilize.menus.slot.SimpleMenuSlot;
import nl.devpieter.utilize.menus.slot.action.SlotClickAction;
import org.bukkit.entity.Player;

import java.util.List;

public class SpecialItemsMenu extends MenuBase {

    public SpecialItemsMenu() {
        super(Component.text("Special Items"), MenuSize.FOUR_ROWS);

        List<ICustomItem> specialItems = ItemManager.getInstance().getSpecialItems();
        for (int i = 0; i < specialItems.size(); i++) {

            ICustomItem item = specialItems.get(i);
            if (item == null || item.getItemStack() == null) continue;

            this.addItem(i, new SimpleMenuSlot(item.getItemStack(), new SlotClickAction().onLeftClick(holder -> {
                Player player = holder.player();

                if (player.getInventory().firstEmpty() != -1) player.getInventory().addItem(item.getItemStack());
                else player.sendMessage(Component.text("You don't have enough space in your inventory!"));
            })));
        }
    }
}
