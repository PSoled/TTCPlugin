package com.totalcraft.soled.Commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class CollectBlocks implements CommandExecutor {
    public static HashMap<String, Integer> collectBlock = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("bcollect")) {
            if (collectBlock.containsKey(player.getName())) {
                int time = collectBlock.get(player.getName());
                player.sendMessage(getPmTTC("&eVocê ainda tem " + time + (time == 1 ? " Minuto" : " Minutos") + " para coletar blocos"));
                return true;
            }

            Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
            if (economy.has(player.getName(), 5000)) {
                economy.withdrawPlayer(player.getName(), 5000);
                collectBlock.put(player.getName(), 10);
                player.sendMessage(getPmTTC("&aVocê comprou 10 Minutos de coletor de blocos por 5000 de money"));
            } else {
                player.sendMessage(getPmTTC("&cÉ meu camarada, tu precisa de 5000 Conto para comprar esta parada"));
            }
        }
        return true;
    }

    public static ScheduledExecutorService schedulerBC = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledBC;

    public static void BCTime() {
        scheduledBC = schedulerBC.scheduleAtFixedRate(() -> {
            Iterator<Map.Entry<String, Integer>> iterator = collectBlock.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                String name = entry.getKey();
                int timeLeft = entry.getValue();

                if (timeLeft < 1) {
                    iterator.remove();
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
}
