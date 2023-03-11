package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class InventoryClick implements Listener {

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getInventory();
        if (clickedInventory != null) {
            ItemStack item = event.getCurrentItem();
            if (item != null && getBanItem(player, item)) {
                event.setCancelled(true);
                clickedInventory.removeItem(item);
            }
        }
    }
}