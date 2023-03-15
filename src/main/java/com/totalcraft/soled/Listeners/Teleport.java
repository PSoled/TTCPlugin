package com.totalcraft.soled.Listeners;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.totalcraft.soled.Utils.GriefPreventionUtils.hasPermClaim;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static org.bukkit.Sound.LEVEL_UP;

public class Teleport implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), LEVEL_UP, 1, 1);
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getTo(), false, null);
        if (claim != null) {
            if (!claim.ownerName.equals(player.getName())) {
                if (hasPermClaim(player, claim)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(getPmTTC("&cVocê não tem permissão para ir no terreno teleportado"));
                }
            }
        }
    }
}
