package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.BflyData;
import com.totalcraft.soled.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
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

        if (cmd.getName().equalsIgnoreCase("sfly")) {
            if (BflyData.flyListPlayer.containsKey(playerName)) {
                if (!player.isFlying()) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(getPmTTC("&aSeu Fly foi reativado"));
                }
                player.sendMessage(getPmTTC("&eVocê ainda tem " + BflyData.flyListPlayer.get(playerName) + " Minutos de Fly"));
                return true;
            }
            Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
            if (economy.has(playerName, 2000)) {
                economy.withdrawPlayer(playerName, 2000);
                player.sendMessage(getPmTTC("&aVocê comprou Fly por 1 Hora Por 2000"));
                player.setAllowFlight(true);
                player.setFlying(true);
                BflyData.flyListPlayer.put(playerName, 60);
                BflyData.saveFlyData();

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
            Iterator<Map.Entry<String, Integer>> iterator = BflyData.flyListPlayer.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                String name = entry.getKey();
                int valor = entry.getValue();
                if (valor < 1) {
                    Player player = Bukkit.getPlayer(name);
                    if (player != null) {
                        player.sendMessage(getPmTTC("&c&lO tempo do seu fly acabou!"));
                        player.setFlying(false);
                        player.setAllowFlight(false);
                    }
                    iterator.remove();
                    BflyData.flyConfig.set(name, null);
                    BflyData.saveFlyData();
                } else {
                    valor--;
                    entry.setValue(valor);
                    BflyData.saveFlyData();
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
}
