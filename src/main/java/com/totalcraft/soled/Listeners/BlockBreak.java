package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.BlockProtectUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static com.totalcraft.soled.Commands.EventoMina.minaPlayers;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        ItemStack itemHand = player.getItemInHand();
        if (block.getLocation().getWorld().getName().equals("minerar")) {
            event.setCancelled(BlockProtectUtils.blockProtectBreak(player, blockLocation));
        }
        if (minaPlayers.contains(player.getName())) {
            if (itemHand.getTypeId() == 30477 || itemHand.getTypeId() == 4386 || itemHand.getTypeId() == 4388) {
                event.setCancelled(true);
                Bukkit.getServer().dispatchCommand(player, "mina sair");
                minaPlayers.remove(player.getName());
                Bukkit.broadcastMessage(getPmTTC(player.getName() + " &Ctentou usar itens errados no Evento Mina Rs"));
            }
        }
    }
}
