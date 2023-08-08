package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.BlockProtectUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

import static com.totalcraft.soled.Commands.CollectBlocks.oresFilter;
import static com.totalcraft.soled.Commands.EventoMina.minaPlayers;
import static com.totalcraft.soled.Commands.FilterBlock.filterChest;
import static com.totalcraft.soled.Configs.FilterBlockData.greenBlock;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockBreak implements Listener {
    private final Plugin plugin;

    public BlockBreak(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        ItemStack itemHand = player.getItemInHand();
        if (block.getLocation().getWorld().getName().equals("minerar")) {
            if (BlockProtectUtils.blockProtectBreak(player, blockLocation)) {
                event.setCancelled(true);
            }
        }
        if (minaPlayers.contains(player.getName())) {
            if (itemHand.getTypeId() == 30477 || itemHand.getTypeId() == 4386 || itemHand.getTypeId() == 4388) {
                event.setCancelled(true);
                Bukkit.getServer().dispatchCommand(player, "mina sair");
                minaPlayers.remove(player.getName());
                Bukkit.broadcastMessage(getPmTTC(player.getName() + " &Ctentou usar itens errados no Evento Mina Rs"));
            }
        }
    }

    List<Integer> toolsGod = Arrays.asList(4386, 4387, 4388, 4389, 4391);
    List<Integer> blockRadius = Arrays.asList(2412);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelDup(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack hand = player.getItemInHand();
        Location location = block.getLocation();
        if (block.getState() instanceof InventoryHolder) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (block.getLocation().getWorld().getName().equals(online.getLocation().getWorld().getName())) {
                    if (online.getLocation().distance(block.getLocation()) <= 7) {
                        online.closeInventory();
                    }
                }
            }
        }
        int radius = 3;
        if (toolsGod.contains(hand.getTypeId())) {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                    for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                        Block loppBlock = location.getWorld().getBlockAt(x, y, z);
                        if (blockRadius.contains(loppBlock.getTypeId())) {
                            player.sendMessage(getPmTTC("&cVocê não pode quebrar com itens god perto de blocos com inventário."));
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void filterBlocKEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        if (world.equals("spawn") || world.equals("world")) {
            return;
        }
        if (filterChest.get(player.getName()) != null) {
            Inventory invFilter = filterChest.get(player.getName());
            if (invFilter.contains(greenBlock)) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    PlayerInventory inventory = player.getInventory();
                    for (int i = 0; i < inventory.getSize(); i++) {
                        boolean continua = false;
                        ItemStack inv = inventory.getItem(i);
                        if (inv != null) {
                            for (ItemStack itemStack : invFilter.getContents()) {
                                if (itemStack != null) {
                                    if (inv.getTypeId() == itemStack.getTypeId() && inv.getDurability() == itemStack.getDurability()) {
                                        continua = true;
                                    }
                                }
                            }
                            if (continua) {
                                continue;
                            }
                            if (invFilter.firstEmpty() > 0) {
                                for (int id : oresFilter) {
                                    if (inv.getTypeId() == id) {
                                        inventory.clear(i);
                                    }
                                }
                            } else {
                                if (inv.getTypeId() == 3 || inv.getTypeId() == 4 || inv.getTypeId() == 13) {
                                    inventory.clear(i);
                                }
                            }
                        }
                    }
                }, 2);
            }
        }
    }
}
