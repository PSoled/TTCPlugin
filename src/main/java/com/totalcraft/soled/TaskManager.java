package com.totalcraft.soled;

import com.totalcraft.soled.Commands.Bfly;
import com.totalcraft.soled.Commands.CollectBlocks;
import com.totalcraft.soled.Commands.Jail;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager {
    private final Plugin plugin;

    public TaskManager(Plugin plugin) {
        this.plugin = plugin;
    }

    BukkitTask TaskDebug;

    public void TaskDebug() {
        TaskDebug = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (Bfly.scheduledBfly == null || Bfly.schedulerBfly.isShutdown()) {
                Bfly.bflyTime();
            }
            if (Jail.scheduledJail == null || Jail.schedulerJail.isShutdown()) {
                Jail.jailTime();
            }
            if (CollectBlocks.scheduledBC == null || CollectBlocks.schedulerBC.isShutdown()) {
                CollectBlocks.BCTime();
            }
        }, 0, 20 * 60);
    }

    public void TaskCancel() {
        if (Bfly.scheduledBfly != null) {
            Bfly.scheduledBfly.cancel(false);
        }
        if (Jail.scheduledJail != null) {
            Jail.scheduledJail.cancel(false);
        }
        if (CollectBlocks.scheduledBC != null) {
            CollectBlocks.scheduledBC.cancel(true);
        }
        if (TaskDebug != null) {
            TaskDebug.cancel();
        }
    }
}
