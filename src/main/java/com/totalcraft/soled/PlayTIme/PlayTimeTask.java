package com.totalcraft.soled.PlayTIme;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.PlayTIme.PlayTimeData.*;
import static com.totalcraft.soled.PlayTIme.PlayerPT.PlayerPTData;
import static com.totalcraft.soled.PlayTIme.PlayerPT.getPlayerPT;
import static com.totalcraft.soled.PlayerManager.PlayerBase.getPlayerBase;
import static com.totalcraft.soled.PlayerManager.RewardData.*;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayTimeTask {
    public static ScheduledExecutorService schedulerPlayTime = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledPlayTimeSec, scheduledPlayTimeMin;
    public static ZoneId saoPauloZone = ZoneId.of("America/Sao_Paulo");
    public static boolean DailyVerifyPT = true;

    public static void initializePlayTime() {
        scheduledPlayTimeSec = schedulerPlayTime.scheduleAtFixedRate(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerPT playerPT = getPlayerPT(player.getName());
                if (playerPT != null) {
                    playerPT.addAll();
                }

            }
        }, 3, 1, TimeUnit.SECONDS);

        scheduledPlayTimeMin = schedulerPlayTime.scheduleAtFixedRate(() -> {
            ZonedDateTime date = ZonedDateTime.now(saoPauloZone);
            int hours = date.getHour();
            int minutes = date.getMinute();
            int weekly = date.getDayOfWeek().getValue();
            int month = date.getDayOfMonth();

//            for (Map.Entry<String, PlayerPT> entry : PlayerPTData.entrySet()) {
//                PlayerPT playerPT = entry.getValue();
//                rewardPlayer(playerPT, playerPT.getDaily(), rewardDaily);
//                rewardPlayer(playerPT, playerPT.getWeekly(), rewardWeekly);
//                rewardPlayer(playerPT, playerPT.getMonth(), rewardMonth);
//                rewardPlayer(playerPT, playerPT.getReset(), rewardReset);
//            }
            if (hours == 6 && DailyVerifyPT) {
                DailyVerifyPT = false;
                PlayTimeConfig.set("DailyVerify", false);
                savePlayTimeFile();
                for (Map.Entry<String, PlayerPT> entry : PlayerPTData.entrySet()) {
                    PlayerPT playerPT = entry.getValue();
                    playerPT.resetDaily();
                    if (weekly == 1) {
                        playerPT.resetWeekly();
                    }
                    if (month == 1) {
                        playerPT.resetMonth();
                    }
                }
            }

            if (hours == 5 && minutes >= 57) {
                DailyVerifyPT = true;
                PlayTimeConfig.set("DailyVerify", true);
                savePlayTimeFile();
            }
            savePlayTime();
        }, 60, 60, TimeUnit.SECONDS);
    }

    private static void rewardPlayer(PlayerPT playerPT, long time, Map<String, ItemStack> map) {
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        if (minutes == 0) {
            for (String s : map.keySet()) {
                String[] split = s.split("-");
                int rewardItem = Integer.parseInt(split[0]);
                if (rewardItem == hours) {
                    Player player = Bukkit.getPlayer(playerPT.getName());
                    if (player != null && split.length > 2) {
                        player.sendMessage(getPmTTC(split[2]));
                    }
                    PlayerBase playerBase = getPlayerBase(playerPT.getName());
                    if (playerBase != null) playerBase.addItemsGive(map.get(s));
//                    playerPT.addAll();
                }
            }
        }

    }
}
