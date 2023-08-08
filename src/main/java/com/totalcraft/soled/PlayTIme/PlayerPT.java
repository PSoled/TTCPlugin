package com.totalcraft.soled.PlayTIme;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerPT {
    private final String name;
    private long daily, weekly, month, reset;
    private long previousDaily, previousWeekly, previousMonth;
    private final List<Long> resetHistoric;
    public PlayerPT(String name) {
        this.name = name;
        daily = 0;
        weekly = 0;
        month = 0;
        reset = 0;
        previousMonth = 0;
        previousWeekly = 0;
        previousDaily = 0;
        resetHistoric = new ArrayList<>();
    }

    public PlayerPT(String name, long daily, long weekly, long month, long reset, long previousDaily, long previousWeekly, long previousMonth, List<Long> resetHistoric) {
        this.name = name;
        this.daily = daily;
        this.weekly = weekly;
        this.month = month;
        this.reset = reset;
        this.previousDaily = previousDaily;
        this.previousWeekly = previousWeekly;
        this.previousMonth = previousMonth;
        this.resetHistoric = resetHistoric;
    }
    public static HashMap<String, PlayerPT> PlayerPTData = new HashMap<>();
    public static void createPlayerPT(String name) {
        PlayerPTData.put(name, new PlayerPT(name));
    }
    public static PlayerPT getPlayerPT(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if (!player.hasPlayedBefore()) return null;
        if (!PlayerPTData.containsKey(name)) createPlayerPT(name);
        return PlayerPTData.get(name);
    }

    public String getName() {
        return name;
    }

    public long getDaily() {
        return daily;
    }

    public long getMonth() {
        return month;
    }

    public long getReset() {
        return reset;
    }

    public long getWeekly() {
        return weekly;
    }

    public long getPreviousDaily() {
        return previousDaily;
    }

    public long getPreviousWeekly() {
        return previousWeekly;
    }

    public long getPreviousMonth() {
        return previousMonth;
    }

    public List<Long> getResetHistoric() {
        return resetHistoric;
    }

    public void addDaily() {
        this.daily += 1;
    }

    public void addMonth() {
        this.month += 1;
    }

    public void addReset() {
        this.reset += 1;
    }

    public void addWeekly() {
        this.weekly += 1;
    }

    public void addAll() {
        addDaily();
        addWeekly();
        addMonth();
        addReset();
    }
    public void resetDaily() {
        this.previousDaily = daily;
        this.daily = 0;
    }
    public void resetWeekly() {
        this.previousWeekly = weekly;
        this.weekly = 0;
    }
    public void resetMonth() {
        this.previousMonth = month;
        this.month = 0;
    }
    public void resetReset() {
        resetHistoric.add(reset);
        this.reset = 0;
    }
}
