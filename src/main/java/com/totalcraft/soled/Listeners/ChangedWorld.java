package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class ChangedWorld implements Listener {
    private final Plugin plugin;

    public ChangedWorld(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return;
        if (playerBase.BFly) {
            String world = player.getWorld().getName();
            if (!world.equals("spawn")) {
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(plugin, () -> {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }, 40L);
            }
        }
    }
}
