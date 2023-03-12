package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static com.totalcraft.soled.Utils.ClaimDeleteUtils.claimContainsBlock;
import static com.totalcraft.soled.Utils.Utils.cancelTpStaff;

public class PlayerCommandPreprocess implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (cancelTpStaff(command, player)) {
            event.setCancelled(true);
        }
        if (Utils.getAdm(player)) {
            if (command.startsWith("/abandonclaim")) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
                if (claimContainsBlock(player, claim, world, false)) {
                    event.setCancelled(true);
                }
            }
            if (command.startsWith("/abandonallclaims")) {
                List<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(player.getName()).claims;
                for (Claim claim : claims) {
                    if (claimContainsBlock(player, claim, world, true)) {
                        event.setCancelled(true);

                    }
                }
            }
        }
    }
}
