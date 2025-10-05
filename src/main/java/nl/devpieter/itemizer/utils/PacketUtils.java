package nl.devpieter.itemizer.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PacketUtils {

    public static @NotNull PacketContainer createBlockBreakPacket(@NotNull Location location, int crackStage, int entityId) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);

        packet.getIntegers().write(0, entityId);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

        packet.getIntegers().write(1, crackStage);

        return packet;
    }

    public static void sendPacketToNearbyPlayers(@NotNull Location location, PacketContainer packet, int radius) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (!(entity instanceof Player player)) continue;
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        }
    }
}
