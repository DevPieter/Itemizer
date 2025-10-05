package nl.devpieter.itemizer.managers;

import nl.devpieter.itemizer.Itemizer;
import nl.devpieter.itemizer.block.IDecayableBlock;
import nl.devpieter.itemizer.runners.DecayRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashMap;

public class DecayableBlockManager {

    private static DecayableBlockManager INSTANCE;

    private final Itemizer plugin = Itemizer.getInstance();

    private final HashMap<Location, IDecayableBlock> blocks = new HashMap<>();

    private DecayableBlockManager() {
    }

    public static DecayableBlockManager getInstance() {
        if (INSTANCE == null) INSTANCE = new DecayableBlockManager();
        return INSTANCE;
    }

    public boolean canPlaceBlock(Location location, BlockState replacedState) {
        if (location == null || replacedState == null) return false;
        if (this.blocks.containsKey(location)) return false;

        Material type = replacedState.getType();
        return type.isAir();
    }

    public boolean isDecayableBlock(Location location) {
        return this.blocks.containsKey(location);
    }

    public void registerBlock(Block block, IDecayableBlock decayableBlock) {
        if (block == null || decayableBlock == null) return;

        Location location = block.getLocation();
        this.blocks.put(location, decayableBlock);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            DecayRunnable runnable = new DecayRunnable(block, Material.AIR, this.blocks::remove);
            runnable.runTaskTimer(plugin, 0L, 10L);
        }, decayableBlock.getDecayTime());
    }
}
