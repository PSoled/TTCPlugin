package com.totalcraft.soled.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static org.bukkit.Sound.LEVEL_UP;

public class Teleport implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), LEVEL_UP, 1, 1);
    }
}
