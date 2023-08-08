package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    @EventHandler
    public void autoFeedEvent(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
            if (playerBase == null) return;
            if (playerBase.AutoFeed) {
                player.setFoodLevel(20);
            }
        }
    }
}
