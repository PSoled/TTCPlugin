package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.totalcraft.soled.Utils.GriefPreventionUtils.getLimitsInClaim;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class ClaimResize implements Listener {

    @EventHandler
    public void onClaimResizeEvent(ClaimResizeEvent event) {
        Claim oldClaim = event.getOldClaim();
        Claim newClaim = event.getNewClaim();
        Player player = Bukkit.getPlayer(oldClaim.getOwnerName());
        if (!Utils.getAdm(player)) {
            return;
        }
        if (newClaim == null) {
            return;
        }
        int oldLimit = getLimitsInClaim(player, oldClaim, false, false);
        int newLimit = getLimitsInClaim(player, newClaim, false, false);
        if (oldLimit != newLimit) {
            event.setCancelled(true);
            player.sendMessage(getPmTTC("&cExiste uma diferença de blocos de limite nesse novo terreno"));
            player.sendMessage(getPmTTC("&fVocê pode digitar &b/blocklimit &fdentro do terreno para ver os blocos de limite nele"));
        }
    }
}
