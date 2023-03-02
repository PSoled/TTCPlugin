package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Sound.NOTE_PLING;

public class RestartServerUtils {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> scheduledFuture;
    int timeRestart = 60;
    public void restartServer() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            if (timeRestart < 20) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"kickall Reniciando-O-Servidor");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                timeRestart();
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), NOTE_PLING, 1, 1);
            }
            String announce = "anunciar &0&l------------------------------------";
            String anunciar = "anunciar &c&lReniciando o Server em &f&l" + timeRestart + " Segundos";
            waitTicks(200);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);
            waitTicks(200);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);
            waitTicks(200);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), anunciar);
            waitTicks(200);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);
            waitTicks(200);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);

            timeRestart -= 20;
        }, 0, 20, TimeUnit.SECONDS);
    }

    public void timeRestart() {
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        },90, 90, TimeUnit.SECONDS);
    }

    public void waitTicks(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
