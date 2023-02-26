package com.totalcraft.soled;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Utils {

    public boolean getAdm(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PermissionUser user = PermissionsEx.getUser(player);
            if (user.has("ttcplugin.admin") || sender.isOp()) {
                return false;
            }
        }
        return !(sender instanceof ConsoleCommandSender);
    }
    public static long getDelayTps(int delay) {
        return (long) (delay / 20 * tps);
    }
    public static double tps;
    public static class TpsTask extends BukkitRunnable {
        private long lastTick = System.currentTimeMillis();
        private int ticks = 0;

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            ticks++;
            if (now - lastTick >= 1000) {
                double tpsHouse = (ticks / ((now - lastTick) / 1000.0));
                tps = Math.round(tpsHouse * 10.0) / 10.0;
                ticks = 0;
                lastTick += 1000 * ((now - lastTick) / 1000);
            }
        }
    }
}
