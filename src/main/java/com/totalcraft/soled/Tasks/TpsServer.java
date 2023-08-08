package com.totalcraft.soled.Tasks;

import java.util.List;
import java.util.concurrent.*;

import static net.minecraft.server.MinecraftServer.currentTPS;

public class TpsServer {
    public static ScheduledExecutorService schedulerTps = new ScheduledThreadPoolExecutor(1);
    public static ScheduledFuture<?> scheduledTps;
    public static double TPS_5SEG, TPS_1MIN, TPS_5MIN, TPS_15MIN, TPS_30MIN;
    private static final List<Double> averageTps5Seg = new CopyOnWriteArrayList<>();
    private static final List<Double> averageTps1Min = new CopyOnWriteArrayList<>();
    private static final List<Double> averageTps5Min = new CopyOnWriteArrayList<>();
    private static final List<Double> averageTps15Min = new CopyOnWriteArrayList<>();
    private static final List<Double> averageTps30Min = new CopyOnWriteArrayList<>();
    public static void tpsHistoric() {
        scheduledTps = schedulerTps.scheduleAtFixedRate(() -> {
            updateTpsList(averageTps5Seg, roundTps(), 5);
            updateTpsList(averageTps1Min, roundTps(), 60);
            updateTpsList(averageTps5Min, roundTps(), 300);
            updateTpsList(averageTps15Min, roundTps(), 900);
            updateTpsList(averageTps30Min, roundTps(), 1800);
            TPS_5SEG = calculateAverageTps(averageTps5Seg);
            TPS_1MIN = calculateAverageTps(averageTps1Min);
            TPS_5MIN = calculateAverageTps(averageTps5Min);
            TPS_15MIN = calculateAverageTps(averageTps15Min);
            TPS_30MIN = calculateAverageTps(averageTps30Min);
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static void updateTpsList(List<Double> list, double tps, int numElements) {
        list.add(tps);
        if (list.size() >= numElements) {
            list.remove(0);
        }
    }

    private static double calculateAverageTps(List<Double> tpsList) {
        double sum = 0;
        for (double tps : tpsList) {
            sum += tps;
        }
        double num = sum / tpsList.size();
        String numFormat = String.format("%.1f", num);
        return Double.parseDouble(numFormat);
    }

    private static double roundTps() {
        double num = currentTPS;
        if (num > 20) {
            num = 20;
        }
        String numFormat = String.format("%.1f", num);
        return Double.parseDouble(numFormat);
    }
}
