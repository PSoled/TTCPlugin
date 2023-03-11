package com.totalcraft.soled;

import com.totalcraft.soled.Commands.Bfly;
import com.totalcraft.soled.Commands.Jail;
import com.totalcraft.soled.Utils.CollectBlocksUtils;

public class TaskManager {

    public static void InitializeTasks() {
        Bfly.bflyTime();
        Jail.jailTime();
        CollectBlocksUtils.collectBlockTime();
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
    }
}
