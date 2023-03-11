package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class InventoryClose implements Listener {

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        getBanItem(player);
    }
}