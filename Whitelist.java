package com.totalcraft.soled.Tasks;

import com.totalcraft.soled.Utils.RestartServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Tasks.TpsServer.TPS_5SEG;

public class Whitelist {
    private final Plugin plugin;

    public Whitelist(Plugin plugin) {
        this.plugin = plugin;
    }
    public static ScheduledExecutorService schedulerRS = new ScheduledThreadPoolExecutor(1);
    public static ScheduledFuture<?> scheduledRS;
    boolean startedServer = false;
    public void whitelistOFF() {
        RestartServerUtils rs = new RestartServerUtils(plugin);
        scheduledRS = schedulerRS.scheduleAtFixedRate(() -> {
            if (!startedServer) {
                if (TPS_5SEG > 11) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
                    scheduledRS.cancel(true);
                    startedServer = true;
                }
            } else {
                scheduledRS.cancel(true);
            }
        },10, 10, TimeUnit.MINUTES);
    }
}
