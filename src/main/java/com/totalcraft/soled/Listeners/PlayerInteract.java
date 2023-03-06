package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
public class PlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item != null && item.getTypeId() == 25284) {
            player.sendMessage(getPmTTC("&cClick do item desativado"));
            event.setCancelled(true);
        }
    }
}
