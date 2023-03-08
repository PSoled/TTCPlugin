package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Configs.BlockProtectData;
import org.bukkit.Location;
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
        Location loc = player.getLocation();
        ItemStack item = player.getItemInHand();
        if (loc.getWorld().getName().equals("minerar")) {
            if (item.getTypeId() == 25284) {
                for (Location locblock : BlockProtectData.protectedBlock.keySet()) {
                    String owner = BlockProtectData.protectedBlock.get(loc);
                    if (!player.getName().equals(owner) && loc.distance(locblock) <= 30) {
                        player.sendMessage(getPmTTC("&cVocê não pode usar este item perto de um bloco protegido!"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
