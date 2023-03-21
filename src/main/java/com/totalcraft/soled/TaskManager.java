package com.totalcraft.soled;

import com.totalcraft.soled.Commands.Bfly;
import com.totalcraft.soled.Commands.Jail;
import com.totalcraft.soled.Commands.RandomTp;
import com.totalcraft.soled.Utils.CollectBlocksUtils;
import com.totalcraft.soled.Utils.VenderUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Listeners.PlayerCommandPreprocess.cooldownCmd;

public class TaskManager {

    public static void InitializeTasks() {
        Bfly.bflyTime();
        Jail.jailTime();
        CollectBlocksUtils.collectBlockTime();
        Times();
    }

    public static void TaskCancel() {
        if (Bfly.scheduledBfly != null) {
            Bfly.scheduledBfly.cancel(false);
        }
        if (Jail.scheduledJail != null) {
            Jail.scheduledJail.cancel(false);
        }
        if (CollectBlocksUtils.scheduledBC != null) {
            CollectBlocksUtils.scheduledBC.cancel(true);
        }
        scheduledTimes.cancel(true);

        if (VenderUtils.scheduledVender != null)  {
            VenderUtils.scheduledVender.cancel(true);
        }

        if (VenderUtils.cancelTask != null) {
            VenderUtils.cancelTask.cancel(true);
        }
    }

    public static ScheduledExecutorService schedulerTimes = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledTimes;

    public static void Times() {
        scheduledTimes = schedulerTimes.scheduleAtFixedRate(() -> {
            Iterator<Map.Entry<String, Integer>> it = RandomTp.cooldown.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                String name = entry.getKey();
                int timeLeft = entry.getValue();
                if (timeLeft < 1) {
                    it.remove();
                } else {
                    RandomTp.cooldown.put(name, timeLeft - 1);
                }
            }

            Iterator<Map.Entry<String, Integer>> itCmd = cooldownCmd.entrySet().iterator();
            while (itCmd.hasNext()) {
                Map.Entry<String, Integer> entry = itCmd.next();
                String name = entry.getKey();
                int timeLeft = entry.getValue();
                if (timeLeft < 1) {
                    itCmd.remove();
                } else {
                    cooldownCmd.put(name, timeLeft - 1);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
