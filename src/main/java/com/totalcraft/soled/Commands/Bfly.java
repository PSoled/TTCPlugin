package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.BflyData;
import com.totalcraft.soled.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class Bfly extends Main implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        String playerName = player.getName();
        String moneytake = "money take " + playerName + " " + 2000;
        Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

        if (cmd.getName().equalsIgnoreCase("sfly")) {
            if (BflyData.listPlayer.containsKey(playerName)) {
                if (!player.isFlying()) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(getPmTTC("&aSeu Fly foi reativado"));
                }
                player.sendMessage(getPmTTC("&eVocê ainda tem " + BflyData.listPlayer.get(playerName)+ " Minutos de Fly"));
                return true;
            }

            if (economy.has(playerName, 2000)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), moneytake);
                player.sendMessage(getPmTTC("&aVocê comprou Fly por 1 Hora Por 2000"));
                player.setAllowFlight(true);
                player.setFlying(true);
                BflyData.listPlayer.put(playerName, 60);
                BflyData.saveFlyData();

                return true;
            } else {
                player.sendMessage(getPmTTC("&cVocê precisa de 2000 de Money para comprar o Fly"));
            }

        }
        return true;
    }

    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static ScheduledFuture<?> scheduledFuture;
    public static void bflyTime() {
        scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            for (String name : BflyData.listPlayer.keySet()) {
                int valor = BflyData.listPlayer.get(name);
                if (valor < 1) {
                    Player player = Bukkit.getPlayer(name);
                    BflyData.listPlayer.remove(name);
                    player.sendMessage(getPmTTC("&c&lO tempo do seu fly acabou!"));
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    BflyData.saveFlyData();
                } else {
                    valor--;
                    BflyData.listPlayer.put(name, valor);
                    BflyData.saveFlyData();
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
    public static void cancelBflyTime() {
        scheduledFuture.cancel(false);
    }
}
