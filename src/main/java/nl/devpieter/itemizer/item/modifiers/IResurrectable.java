package nl.devpieter.itemizer.item.modifiers;

import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IResurrectable {

    default void onPlayerResurrect(ItemStack itemStack, EntityResurrectEvent event) {
    }

    default List<ConsumeEffect> getDeathEffects() {
        return List.of();
    }
}
