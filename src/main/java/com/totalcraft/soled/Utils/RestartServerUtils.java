package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class RestartServerUtils {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> scheduledFuture;
    int timeRestart = 60;
    public void restartServer() {
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            if (timeRestart == 0) {
                scheduledFuture.cancel(true);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"kickall " + getPmTTC("&e&lReniciando-o-Servidor"));
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
                timeRestart();
                return;
            }
            String announce = "anunciar ------------------------------------";
            String anunciar = "anunciar &c&lReniciando o Server em &e&l" + timeRestart + " Segundos";
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), anunciar);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), announce);

            timeRestart -= 20;
        }, 0, 20, TimeUnit.SECONDS);
    }

    public void timeRestart() {
        scheduledFuture = scheduler.schedule(() -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 90, TimeUnit.SECONDS);
    }
}
