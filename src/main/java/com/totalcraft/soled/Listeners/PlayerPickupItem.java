package com.totalcraft.soled.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import static com.totalcraft.soled.Commands.CollectBlocks.oresFilter;
import static com.totalcraft.soled.Commands.FilterBlock.filterChest;
import static com.totalcraft.soled.Configs.FilterBlockData.greenBlock;
import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class PlayerPickupItem implements Listener {
    private final Plugin plugin;

    public PlayerPickupItem(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void banItemEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        if (getBanItem(player, item)) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                getBanItem(player);
                event.getItem().remove();
                player.getInventory().removeItem(item);
            });
        }
    }

    @EventHandler
    public void filterBlocKEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        if (world.equals("spawn") || world.equals("world")) {
            return;
        }
        Item item = event.getItem();
        ItemStack itemS = item.getItemStack();
        if (itemS != null) {
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
}

