package com.totalcraft.soled;

import com.totalcraft.soled.Commands.Bfly;
import com.totalcraft.soled.Commands.CollectBlocks;
import com.totalcraft.soled.Commands.Jail;

public class TaskManager {

    public static void InitializeTasks() {
        Bfly.bflyTime();
        Jail.jailTime();
        CollectBlocks.BCTime();
    }

    public static void TaskCancel() {
        if (Bfly.scheduledBfly != null) {
            Bfly.scheduledBfly.cancel(false);
        }
        if (Jail.scheduledJail != null) {
            Jail.scheduledJail.cancel(false);
        }
        if (CollectBlocks.scheduledBC != null) {
            CollectBlocks.scheduledBC.cancel(true);
        }
    }
}
