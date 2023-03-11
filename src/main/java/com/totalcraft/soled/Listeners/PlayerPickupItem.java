package com.totalcraft.soled.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class PlayerPickupItem implements Listener {
    private final Plugin plugin;

    public PlayerPickupItem(Plugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
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

}
