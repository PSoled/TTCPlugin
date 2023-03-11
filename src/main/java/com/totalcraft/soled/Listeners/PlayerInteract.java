package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.BlockProtectUtils;
import com.totalcraft.soled.Utils.CollectBlocksUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Utils.BanItemUtils.getBanItem;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        ItemStack item = player.getItemInHand();
        if (!player.getWorld().getName().equals("world") && !player.getWorld().getName().equals("spawn")) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                CollectBlocksUtils.collectBlockInteract(player);
            }
        }
        if (loc.getWorld().getName().equals("minerar")) {
            event.setCancelled(BlockProtectUtils.blockProtectInteract(player, loc, item));
        }
        if (getBanItem(player, item)) {
            event.setCancelled(true);
        }
    }
}
