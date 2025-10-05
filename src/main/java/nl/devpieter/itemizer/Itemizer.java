package nl.devpieter.itemizer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import nl.devpieter.itemizer.item.modifiers.IDualWieldable;
import nl.devpieter.itemizer.listeners.CustomItemListener;
import nl.devpieter.itemizer.listeners.DecayableBlockListener;
import nl.devpieter.itemizer.listeners.ResurrectableItemListener;
import nl.devpieter.itemizer.listeners.ShootableItemListener;
import nl.devpieter.itemizer.managers.ItemManager;
import nl.devpieter.itemizer.menus.SpecialItemsMenu;
import nl.devpieter.itemizer.runners.CleanupRunner;
import nl.devpieter.itemizer.runners.DayNightCycleRunner;
import nl.devpieter.itemizer.runners.HoldingRunner;
import nl.devpieter.utilize.menus.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public final class Itemizer extends JavaPlugin {

    private static Itemizer INSTANCE;

    private final Logger logger = this.getLogger();
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.logger.info("Starting Itemizer...");
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        this.logger.info("Registering commands");
        LiteralArgumentBuilder<CommandSourceStack> itemizerCommand = Commands.literal("itemizer")
                .requires(sender -> sender.getSender().hasPermission("itemizer.items.menu"))
                .executes(context -> {
                    if (!(context.getSource().getExecutor() instanceof Player player)) return 0;

                    MenuManager menuManager = MenuManager.getInstance();
                    menuManager.openMenu(player, new SpecialItemsMenu());

                    return 1;
                });

        LiteralArgumentBuilder<CommandSourceStack> durabilityCommand = Commands.literal("durability")
                .requires(sender -> sender.getSender().hasPermission("itemizer.items.durability"))
                .then(Commands.argument("max", BoolArgumentType.bool())
                        .then(Commands.argument("durability", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    if (!(context.getSource().getExecutor() instanceof Player player)) return 0;

                                    boolean setMax = BoolArgumentType.getBool(context, "max");
                                    int durability = IntegerArgumentType.getInteger(context, "durability");

                                    if (durability < 0) {
                                        player.sendMessage("Durability must be greater than 0!");
                                        return 1;
                                    }

                                    ItemStack item = player.getInventory().getItemInMainHand();
                                    if (item.getType().isAir()) {
                                        player.sendMessage("You must be holding an item!");
                                        return 1;
                                    }

                                    if (!(item.getItemMeta() instanceof Damageable meta)) {
                                        player.sendMessage("This item has no meta!");
                                        return 1;
                                    }

                                    if (setMax) meta.setMaxDamage(durability);
                                    else meta.setDamage(durability);

                                    item.setItemMeta(meta);

                                    if (setMax)
                                        player.sendMessage("Set the max durability of " + item.getType().name() + " to " + durability + "!");
                                    else
                                        player.sendMessage("Set the durability of " + item.getType().name() + " to " + durability + "!");

                                    return 1;
                                })));

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(itemizerCommand.build(), "itemizer", List.of("specialitems", "specialitem"));
            commands.registrar().register(durabilityCommand.build(), "itemizer");
        });

        this.logger.info("Registering listeners");
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new CustomItemListener(), this);
        manager.registerEvents(new DecayableBlockListener(), this);
        manager.registerEvents(new ResurrectableItemListener(), this);

        ShootableItemListener shootableItemListener = new ShootableItemListener();
        manager.registerEvents(shootableItemListener, this);

        Bukkit.getScheduler().runTaskTimer(Itemizer.getInstance(), new DayNightCycleRunner(), 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(Itemizer.getInstance(), new HoldingRunner(), 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(Itemizer.getInstance(), new CleanupRunner(shootableItemListener), 0L, 30 * 20L);

        ItemManager itemManager = ItemManager.getInstance();

        this.protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EQUIPMENT) {

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int senderId = packet.getIntegers().read(0);

                Player sender = null;
                for (Player player : Bukkit.getOnlinePlayers()) if (player.getEntityId() == senderId) sender = player;
                if (sender == null) return;

                PacketContainer newItemPacket = packet.deepClone();
                PlayerInventory inventory = sender.getInventory();

                ItemStack mainHand = inventory.getItemInMainHand();
                boolean isDualWieldable = itemManager.findSpecialItem(mainHand) instanceof IDualWieldable;

                ItemStack offHand = inventory.getItemInOffHand();
                ItemStack newOffHand = offHand.isEmpty() ? isDualWieldable ? mainHand : offHand : offHand;

                List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = newItemPacket.getSlotStackPairLists().read(0);
                list.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, newOffHand));

                newItemPacket.getIntegers().write(0, sender.getEntityId());
                newItemPacket.getSlotStackPairLists().write(0, list);

                event.setPacket(newItemPacket);
                protocolManager.sendServerPacket(sender, newItemPacket, false);
            }
        });
    }

    @Override
    public void onDisable() {
        this.logger.info("Stopping Itemizer...");
        this.protocolManager.removePacketListeners(this);
        this.getServer().getScheduler().cancelTasks(this);
    }

    public static Itemizer getInstance() {
        return INSTANCE;
    }
}
