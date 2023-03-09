package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static org.bukkit.Sound.NOTE_PLING;

public class RestartServerUtils {

    private final Plugin plugin;

    public RestartServerUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    BukkitTask restart;
    static int timeRestart = 60;

    public void restartServer() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
        restart = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (timeRestart < 20) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(getPmTTC("&cServidor Reniciando"));
                }
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart"), 20 * 50);
                restart.cancel();
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), NOTE_PLING, 1, 1);
            }

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "anunciar &f&l------------------------------------");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "anunciar &f&l------------------------------------");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "anunciar &c&lReniciando o Server em &f&l" + timeRestart + " Segundos");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "anunciar &f&l------------------------------------");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "anunciar &f&l------------------------------------");
            timeRestart -= 20;
        }, 0, 20 * 15);
    }
}


