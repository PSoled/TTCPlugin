package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.CollectBlocks;
import com.totalcraft.soled.Configs.BlockProtectData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if (!player.getWorld().getName().equals("world") && !player.getWorld().getName().equals("spawn")) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (CollectBlocks.collectBlock.containsKey(player.getName())) {
                    for (Entity ItemCB : player.getNearbyEntities(7, 7, 7)) {
                        if (ItemCB instanceof Item) {
                            Item itemStack = (Item) ItemCB;
                            if (CollectBlocks.BlockFilter.containsKey(player.getName())) {
                                int id = CollectBlocks.BlockFilter.get(player.getName());
                                if (!(itemStack.getItemStack().getTypeId() == id)) {
                                    if (CollectBlocks.oresFilter.contains(itemStack.getItemStack().getTypeId())) {
                                        ItemCB.remove();
                                    }
                                } else {
                                    ItemCB.teleport(player);
                                }
                            } else {
                                if (itemStack.getItemStack().getTypeId() == 4 || itemStack.getItemStack().getTypeId() == 3 || itemStack.getItemStack().getTypeId() == 13) {
                                    ItemCB.remove();
                                } else {
                                    ItemCB.teleport(player);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
