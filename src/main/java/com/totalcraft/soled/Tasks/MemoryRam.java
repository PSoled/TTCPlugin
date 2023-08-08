package com.totalcraft.soled.Tasks;

import java.util.List;
import java.util.concurrent.*;

public class MemoryRam {

    public static ScheduledExecutorService schedulerRam = new ScheduledThreadPoolExecutor(1);
    public static ScheduledFuture<?> scheduledRam;
    public static int RAM_5SEG, RAM_1MIN, RAM_5MIN, RAM_15MIN, RAM_30MIN;
    private static final List<Integer> averageRam5Seg = new CopyOnWriteArrayList<>();
    private static final List<Integer> averageRam1Min = new CopyOnWriteArrayList<>();
    private static final List<Integer> averageRam5Min = new CopyOnWriteArrayList<>();
    private static final List<Integer> averageRam15Min = new CopyOnWriteArrayList<>();
    private static final List<Integer> averageRam30Min = new CopyOnWriteArrayList<>();
    public static void ramHistoric() {
        scheduledRam = schedulerRam.scheduleAtFixedRate(() -> {
            updateRamList(averageRam5Seg, roundRam(), 5);
            updateRamList(averageRam1Min, roundRam(), 60);
            updateRamList(averageRam5Min, roundRam(), 300);
            updateRamList(averageRam15Min, roundRam(), 900);
            updateRamList(averageRam30Min, roundRam(), 1800);
            RAM_5SEG = calculateAverageRam(averageRam5Seg);
            RAM_1MIN = calculateAverageRam(averageRam1Min);
            RAM_5MIN = calculateAverageRam(averageRam5Min);
            RAM_15MIN = calculateAverageRam(averageRam15Min);
            RAM_30MIN = calculateAverageRam(averageRam30Min);
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static void updateRamList(List<Integer> list, int ram, int numElements) {
        list.add(ram);
        if (list.size() >= numElements) {
            list.remove(0);
        }
    }

    private static int calculateAverageRam(List<Integer> ramList) {
        double sum = 0;
        for (int ram : ramList) {
            sum += ram;
        }
        double num = sum / ramList.size();
        return (int) num;
    }

    private static int roundRam() {
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long usedMemory = totalMemory - freeMemory;
        double usedMemoryMB = (double) usedMemory / (1024 * 1024);
        String useMem = String.valueOf(usedMemoryMB);
        return Integer.parseInt(useMem.split("\\.")[0]);
    }
}
