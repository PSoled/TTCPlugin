package com.totalcraft.soled.Tasks;

import com.totalcraft.soled.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class AfkPlayer {

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledAfk;
    public static Map<String, Integer> timeAfk = new HashMap<>();
    private static final Map<String, Location> playersLoc = new HashMap<>();

    public static void startAfkTimer() {
        scheduledAfk = scheduler.scheduleAtFixedRate(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                    boolean create = true;
                    Location loc = player.getLocation();
                    if (timeAfk.containsKey(player.getName())) {
                        Location checkLoc = playersLoc.get(player.getName());
                        if (checkLoc.getX() == loc.getX() && checkLoc.getY() == loc.getY() && checkLoc.getZ() == loc.getZ() ) {
                            create = false;
                            int time = timeAfk.get(player.getName()) + 1;
                            timeAfk.put(player.getName(), time);
                            if (time > 14 && teleportPlayer(player)) {
                                playersLoc.put(player.getName(), loc);
                                player.sendMessage("");
                                player.sendMessage(getPmTTC("&cVocê foi teleportado pro spawn por afk."));
                                player.sendMessage(getPmTTC("&cNão utilize sistemas de anti-afk, há punições para isso."));
                                player.sendMessage("");
                                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                                scheduler.scheduleSyncDelayedTask(Main.get(), () -> {
                                    player.teleport(new Location(Bukkit.getWorld("spawn"), -57, 86, -155));
                                }, 0L);

                            }
                        }
                    }
                    if (create) {
                        timeAfk.put(player.getName(), 0);
                        playersLoc.put(player.getName(), loc);
                    }
                }
//                for (String nick : timeAfk.keySet()) {
//                    if (!listNicks.contains(nick)) {
//                        timeAfk.remove(nick);
//                        playersLoc.remove(nick);
//                    }
//                }
        }, 1, 1, TimeUnit.MINUTES);
    }

    private static boolean teleportPlayer(Player player) {
        PermissionUser user = PermissionsEx.getUser(player);
        String world = player.getWorld().getName();
        return !user.has("totalcraft.bypass.afk") && getAdm(player) && !world.equals("spawn");
    }
}