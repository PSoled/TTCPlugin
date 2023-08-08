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

public class Bfly implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return true;
        if (cmd.getName().equalsIgnoreCase("sfly")) {
            if (playerBase.BFly) {
                if (!player.isFlying()) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(getPmTTC("&aSeu Fly foi reativado"));
                }
                String minute = playerBase.getBFlyTime() > 1 ? "Minutos" : "Minuto";
                player.sendMessage(getPmTTC("&eVocê ainda tem " + playerBase.getBFlyTime() + " "  + minute + " de Fly"));
                return true;
            }
            Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
            if (economy.has(player.getName(), 2000)) {
                economy.withdrawPlayer(player.getName(), 2000);
                player.sendMessage(getPmTTC("&aVocê comprou Fly por 1 Hora Por 2000"));
                player.setAllowFlight(true);
                player.setFlying(true);
                playerBase.setBFlyTime(60);
                playerBase.BFly = true;
                playerBase.saveData();
                return true;
            } else {
                player.sendMessage(getPmTTC("&cÉ meu camarada, tu precisa de 2000 Conto para comprar esta parada"));
            }
        }
        return true;
    }
    public static ScheduledExecutorService schedulerBfly = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture<?> scheduledBfly;
    public static void bflyTime() {
        scheduledBfly = schedulerBfly.scheduleAtFixedRate(() -> {
            for (Map.Entry<String, PlayerBase> entry : playersBase.entrySet()) {
                String name = entry.getKey();
                PlayerBase playerBase = entry.getValue();
                int time = playerBase.getBFlyTime();
                if (playerBase.BFly && time == 0) {
                    Player player = Bukkit.getPlayer(name);
                    if (player != null) {
                        player.sendMessage(getPmTTC("&c&lO tempo do seu fly acabou!"));
                        player.setFlying(false);
                        player.setAllowFlight(false);
                    }
                    playerBase.BFly = false;
                    playerBase.saveData();
                }
                if (playerBase.BFly) {
                    time--;
                    playerBase.setBFlyTime(time);
                    playerBase.saveData();
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
}
