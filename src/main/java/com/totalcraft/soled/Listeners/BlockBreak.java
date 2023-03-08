package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.BlockProtect;
import com.totalcraft.soled.Commands.CollectBlocks;
import com.totalcraft.soled.Commands.EventoMina;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        ItemStack itemHand = player.getItemInHand();
        if (block.getLocation().getWorld().getName().equals("minerar")) {
            event.setCancelled(BlockProtect.blockProtectBreak(player, blockLocation));
        }
        if (EventoMina.minaPlayers.contains(player.getName())) {
            if (itemHand.getTypeId() == 30477 || itemHand.getTypeId() == 4386 || itemHand.getTypeId() == 4388) {
                event.setCancelled(true);
                Bukkit.getServer().dispatchCommand(player, "mina sair");
                EventoMina.minaPlayers.remove(player.getName());
                Bukkit.broadcastMessage(getPmTTC(player.getName() + " &Ctentou usar itens errados no Evento Mina Rs"));
            }
        }
        if (!player.getWorld().getName().equals("world") && !player.getWorld().getName().equals("spawn")) {
            if (CollectBlocks.collectBlock.containsKey(player.getName())) {
                for (Entity item : player.getNearbyEntities(7, 7, 7)) {
                    if (item instanceof Item) {
                        item.teleport(player);
                    }
                }
            }
        }
    }
}
