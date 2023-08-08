package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.PlayerManager.PlayerBase.playersBase;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class AutoFeed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return true;
        if (playerBase.AutoFeed) {
            int value = playerBase.getAutoFeedTime();
            player.sendMessage(getPmTTC("&aVocê ainda tem " + value + " Minutos de auto feed"));
        } else {
            Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
            if (!economy.has(player.getName(), 10000)) {
                player.sendMessage(getPmTTC("&cVocê precisa de 10000 de money para comprar o auto feed"));
                return true;
            }
            economy.withdrawPlayer(player.getName(), 10000);
            player.sendMessage(getPmTTC("&aVocê comprou o auto feed com Sucesso!"));
            playerBase.setAutoFeedTime(60);
            playerBase.AutoFeed = true;
            playerBase.saveData();
        }
        return true;
    }

    public static ScheduledExecutorService schedulerAutoFeed = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledAutoFeed;

    public static void autoFeedTime() {
        scheduledAutoFeed = schedulerAutoFeed.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, PlayerBase> entry : playersBase.entrySet()) {
                String name = entry.getKey();
                PlayerBase playerBase = entry.getValue();
                int time = playerBase.getAutoFeedTime();
                if (playerBase.AutoFeed && time == 0) {
                    Player player = Bukkit.getPlayer(name);
                    if (player != null) {
                        player.sendMessage(getPmTTC("&cO tempo do seu auto feed acabou!"));
                    }
                    playerBase.AutoFeed = false;
                    playerBase.saveData();
                }
                if (playerBase.AutoFeed) {
                    time--;
                    playerBase.setAutoFeedTime(time);
                    playerBase.saveData();
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
}
