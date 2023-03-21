package com.totalcraft.soled.Utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Commands.CollectBlocks.collectBlock;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class CollectBlocksUtils {

    public static void reimbursementMoney() {
        if (collectBlock != null) {
            Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
            for (String name : collectBlock.keySet()) {
                Player player = Bukkit.getPlayer(name);
                economy.depositPlayer(name, 5000);
                player.sendMessage(getPmTTC("&eSeu Bcollect foi reembolsado, Porque o soled ta mexendo no Plugin rs"));
            }
        }
    }

    public static ScheduledExecutorService schedulerBC = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledBC;

    public static void collectBlockTime() {
        scheduledBC = schedulerBC.scheduleAtFixedRate(() -> {
            Iterator<Map.Entry<String, Integer>> it = collectBlock.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                String name = entry.getKey();
                int timeLeft = entry.getValue();
                if (timeLeft < 1) {
                    it.remove();
                    Player player = Bukkit.getPlayer(name);
                    if (player != null) {
                        player.sendMessage(getPmTTC("&cAcabou o tempo do seu coletor de blocos"));
                    }
                } else {
                    collectBlock.put(name, timeLeft - 1);
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    public static void collectBlockInteract(Player player) {
        if (collectBlock.containsKey(player.getName())) {
            for (Entity ItemCB : player.getNearbyEntities(10, 10, 10)) {
                if (ItemCB instanceof Item) {
                    ItemCB.teleport(player.getLocation());
                }
            }
        }
    }
}

