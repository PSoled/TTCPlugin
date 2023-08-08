package com.totalcraft.soled.Tasks;

import com.totalcraft.soled.Commands.Bfly;
import com.totalcraft.soled.Commands.Jail;
import com.totalcraft.soled.Commands.RandomTp;
import com.totalcraft.soled.Utils.CollectBlocksUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Commands.AutoFeed.autoFeedTime;
import static com.totalcraft.soled.Commands.AutoFeed.scheduledAutoFeed;
import static com.totalcraft.soled.Commands.Bfly.scheduledBfly;
import static com.totalcraft.soled.Commands.Jail.scheduledJail;
import static com.totalcraft.soled.Configs.ItemPrivateLog.autoSaveLog;
import static com.totalcraft.soled.Configs.ItemPrivateLog.scheduledLog;
import static com.totalcraft.soled.PlayTIme.PlayTimeTask.*;
import static com.totalcraft.soled.Tasks.AfkPlayer.scheduledAfk;
import static com.totalcraft.soled.Tasks.AfkPlayer.startAfkTimer;
import static com.totalcraft.soled.Tasks.MemoryRam.ramHistoric;
import static com.totalcraft.soled.Tasks.MemoryRam.scheduledRam;
import static com.totalcraft.soled.Tasks.TpsServer.scheduledTps;
import static com.totalcraft.soled.Tasks.TpsServer.tpsHistoric;
import static com.totalcraft.soled.Utils.CollectBlocksUtils.scheduledBC;
import static com.totalcraft.soled.Utils.VenderUtils.cancelTask;
import static com.totalcraft.soled.Utils.VenderUtils.scheduledVender;

public class TaskManager {
    public void InitializeTasks() {
        Bfly.bflyTime();
        Jail.jailTime();
        CollectBlocksUtils.collectBlockTime();
        Times();
        autoSaveLog();
        tpsHistoric();
        ramHistoric();
        autoFeedTime();
        startAfkTimer();
        initializePlayTime();
    }
    private static void cancel(ScheduledFuture<?> scheduledFuture) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    public static void TaskCancel() {
        cancel(scheduledBfly);
        cancel(scheduledJail);
        cancel(scheduledBC);
        cancel(scheduledTimes);
        cancel(scheduledVender);
        cancel(cancelTask);
        cancel(scheduledLog);
        cancel(scheduledTps);
        cancel(scheduledAutoFeed);
        cancel(scheduledRam);
        cancel(scheduledAfk);
        cancel(scheduledPlayTimeSec);
        cancel(scheduledPlayTimeMin);
    }

    public static ScheduledExecutorService schedulerTimes = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledTimes;

    public void Times() {
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
        }, 0, 1, TimeUnit.SECONDS);
//        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
//            try {
//                botTotalCraft = JDABuilder.createDefault("NzE5NzQwNzM3NTUzNjk0NzIw.GzxxtZ.YzzfGuFuVgh6hlx4s3Gcv15A3szFDMaKFBq-h0").build();
//                botTest = JDABuilder.createDefault("MTEwMTMwMjU2MTUxMzk1MTMxMg.GTg3ru.pC4F4qvYMk7Zh76SJPG6rtj2Je1ajCSWw5KZ-E").build();
//                System.out.println("Bots criados com sucesso!");
//            } catch (Exception e) {
//                System.out.println("Erro ao criar bots: " + e.getMessage());
//            }
//        });
    }
}
