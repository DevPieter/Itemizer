package nl.devpieter.itemizer.listeners;

import nl.devpieter.itemizer.block.IDecayableBlock;
import nl.devpieter.itemizer.item.ICustomItem;
import nl.devpieter.itemizer.managers.DecayableBlockManager;
import nl.devpieter.itemizer.managers.ItemManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class DecayableBlockListener implements Listener {

    private final DecayableBlockManager blockManager = DecayableBlockManager.getInstance();
    private final ItemManager itemManager = ItemManager.getInstance();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        ItemStack itemStack = event.getItemInHand();
        ICustomItem specialItem = this.itemManager.findSpecialItem(itemStack);

        if (specialItem == null) {
            // TODO - Check if player is allowed to place this block

            Player player = event.getPlayer();
            if (player.isOp() && player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (!(specialItem instanceof IDecayableBlock decayableBlock)) {
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        Location location = block.getLocation();
        BlockState replacedState = event.getBlockReplacedState();

        if (!this.blockManager.canPlaceBlock(location, replacedState)) {
            event.setCancelled(true);
            return;
        }

        this.blockManager.registerBlock(block, decayableBlock);
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
        if (event.isCancelled()) return;

        Block source = event.getSourceBlock();
        if (!this.blockManager.isDecayableBlock(source.getLocation())) return;

        event.setCancelled(true);
    }
}
