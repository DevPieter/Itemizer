package nl.devpieter.itemizer.runners;

import com.comphenix.protocol.events.PacketContainer;
import nl.devpieter.itemizer.utils.PacketUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DecayRunnable extends BukkitRunnable {

    private static final int MAX_CRACK_STAGE = 9;

    private final Block currentBlock;
    private final Material decaysTo;

    private final Location location;
    private final int entityId;

    private final Consumer<Location> onDecayComplete;

    private int crackStage = 0;

    public DecayRunnable(@NotNull Block block, Material decaysTo, Consumer<Location> onDecayComplete) {
        this.currentBlock = block;
        this.decaysTo = decaysTo;

        this.location = block.getLocation();
        this.entityId = this.generateEntityId(location);

        this.onDecayComplete = onDecayComplete;
    }

    @Override
    public void run() {
        if (this.currentBlock == null) {
            this.cancel();

            this.onDecayComplete.accept(this.location);
            return;
        }

        if (this.crackStage < MAX_CRACK_STAGE) {
            PacketContainer packet = PacketUtils.createBlockBreakPacket(this.location, this.crackStage, this.entityId);
            PacketUtils.sendPacketToNearbyPlayers(this.location, packet, 50);

            crackStage++;
        } else {
            this.currentBlock.getWorld().playEffect(this.location, Effect.STEP_SOUND, this.currentBlock.getType());
            this.currentBlock.setType(this.decaysTo, false);

            PacketContainer packet = PacketUtils.createBlockBreakPacket(location, -1, entityId);
            PacketUtils.sendPacketToNearbyPlayers(location, packet, 50);

            this.cancel();
            this.onDecayComplete.accept(this.location);
        }
    }

    private int generateEntityId(@NotNull Location loc) {
        return loc.getBlockX() * 1000000 + loc.getBlockY() * 1000 + loc.getBlockZ();
    }
}
