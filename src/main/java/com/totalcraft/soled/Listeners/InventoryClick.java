package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Commands.FilterBlock.filterChest;
import static com.totalcraft.soled.Configs.FilterBlockData.greenBlock;
import static com.totalcraft.soled.Configs.FilterBlockData.redBlock;
import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class InventoryClick implements Listener {

    @EventHandler
    public void banItemEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        if (inv != null) {
            ItemStack item = event.getCurrentItem();
            if (item != null && getBanItem(player, item)) {
                event.setCancelled(true);
                inv.removeItem(item);
            }
        }
    }


    @EventHandler
    public void filterBlocKEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        Inventory invFilter = filterChest.get(player.getName());
        if (inv != null && invFilter != null) {
            if (invFilter.getName().equals(inv.getName())) {
                ItemStack item = event.getCurrentItem();
                if (item != null) {
                    event.setCancelled(true);
                    if (item.getTypeId() == 35 && item.getDurability() == 14) {
                        invFilter.setItem(17, greenBlock);
                        return;
                    } else if (item.getTypeId() == 35 && item.getDurability() == 5) {
                        invFilter.setItem(17, redBlock);
                        return;
                    }
                    ItemStack newItem = new ItemStack(item.getTypeId(), 1, item.getDurability());
                    if (invFilter.contains(newItem)) {
                        invFilter.remove(newItem);
                        return;
                    }
                    int count = 0;
                    for (ItemStack itemStack : invFilter.getContents()) {
                        if (itemStack == null) {
                            invFilter.setItem(count, newItem);
                            break;
                        }
                        count++;
                    }
                }
            }
        }
    }
}