package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (getBanItem(player, item)) {
            event.getItemDrop().remove();
        }
    }
}
