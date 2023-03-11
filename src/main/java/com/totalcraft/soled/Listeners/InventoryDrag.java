package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class InventoryDrag implements Listener {

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (getBanItem(player)) {
            event.setCancelled(true);
        }
    }
}
