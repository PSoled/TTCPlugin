package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Commands.CollectBlocks;
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

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class CollectBlocksUtils {
    public static ScheduledExecutorService schedulerBC = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledBC;

    public static void collectBlockTime() {
        scheduledBC = schedulerBC.scheduleAtFixedRate(() -> {
            Iterator<Map.Entry<String, Integer>> it = CollectBlocks.collectBlock.entrySet().iterator();
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
                    CollectBlocks.collectBlock.put(name, timeLeft - 1);
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    public static void collectBlockInteract(Player player) {
        if (CollectBlocks.collectBlock.containsKey(player.getName())) {
            for (Entity ItemCB : player.getNearbyEntities(7, 7, 7)) {
                if (ItemCB instanceof Item) {
                    Item itemStack = (Item) ItemCB;
                    if (CollectBlocks.BlockFilter.containsKey(player.getName())) {
                        int id = CollectBlocks.BlockFilter.get(player.getName());
                        if (!(itemStack.getItemStack().getTypeId() == id)) {
                            if (CollectBlocks.oresFilter.contains(itemStack.getItemStack().getTypeId())) {
                                ItemCB.remove();
                            }
                        } else {
                            ItemCB.teleport(player);
                        }
                    } else {
                        if (itemStack.getItemStack().getTypeId() == 4 || itemStack.getItemStack().getTypeId() == 3 || itemStack.getItemStack().getTypeId() == 13) {
                            ItemCB.remove();
                        } else {
                            ItemCB.teleport(player);
                        }
                    }
                }
            }
        }
    }
}
