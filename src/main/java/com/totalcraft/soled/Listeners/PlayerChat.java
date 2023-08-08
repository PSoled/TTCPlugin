package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static com.totalcraft.soled.Commands.SetHome.questionHome;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerChat implements Listener {

    @EventHandler
    public void setHomeQuestion(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        if (questionHome.contains(player.getName())) {
            questionHome.remove(player.getName());
            event.setCancelled(true);
            PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
            if (playerBase == null) return;
            if (msg.equalsIgnoreCase("sim")) {
                playerBase.addHome(playerBase.getCacheHomeName(), playerBase.getCacheHomeLoc());
                playerBase.setCacheHomeName(null);
                playerBase.setCacheHomeLoc(null);
                playerBase.saveData();
                player.sendMessage(getPmTTC("&aVocê sobrescreveu o sethome com Sucesso!"));
            } else {
                player.sendMessage(getPmTTC("&eCancelado o sobrescrever do sethome"));
                playerBase.setCacheHomeName(null);
                playerBase.setCacheHomeLoc(null);
            }
        }
    }
}
