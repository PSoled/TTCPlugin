package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import static com.totalcraft.soled.Commands.EventoMina.minaPlayers;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerToggleFlight implements Listener {

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (Utils.getAdm(player)) {
            if (minaPlayers.contains(player.getName())) {
                if (event.isFlying()) {
                    player.sendMessage(getPmTTC("&cSeu fly foi desativado no evento"));
                    player.setFlying(false);
                    event.setCancelled(true);
                }
            }
        }
    }
}
