package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Sound.NOTE_PLING;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class RestartServerUtils {
    int timeRestart = 60;
    ScheduledExecutorService restartScheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> restartFuture;

    public void restartServer() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
        restartFuture = restartScheduler.scheduleAtFixedRate(() -> {
            if (timeRestart < 20) {
                restartFuture.cancel(true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(getPmTTC("&cReniciando o Servidor"));
                }
                restartFuture = restartScheduler.schedule(() -> {
                    Bukkit.reload();
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                    restartFuture.cancel(true);
                    restartFuture = restartScheduler.schedule(() -> {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
                        restartFuture.cancel(true);
                    }, 60, TimeUnit.SECONDS);
                }, 30, TimeUnit.SECONDS);
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
        }, 0, 20, TimeUnit.SECONDS);
    }
}


