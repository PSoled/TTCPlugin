package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class CollectBlocks implements CommandExecutor {
    public static HashMap<String, Integer> collectBlock = new HashMap<>();
    public static List<Integer> oresFilter = Arrays.asList(49, 66, 287, 85, 5, 2761, 50, 255, 4, 15, 16, 2762, 24, 3, 12, 249, 4362, 2402, 2001, 13, 1750, 14, 351, 331, 264, 388, 30243, 248, 4329, 3346, 4321, 4322, 4323, 4324, 4325, 4326, 4327, 4328, 714, 4330, 4345, 1475, 2100, 25264, 6278, 6319, 6320, 25263, 263, 318, 280, 27972, 2761);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        if (!MainConfig.bcollectModule) {
            sender.sendMessage(getPmTTC("&cComando Bcollect Desativado"));
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("bcollect")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "tempo":
                case "comprar":
                case "filtro":
                    break;
                default:
                    sender.sendMessage(getCommmandoCB());
                    break;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("tempo")) {
                if (collectBlock.containsKey(player.getName())) {
                    int time = collectBlock.get(player.getName());
                    player.sendMessage(getPmTTC("&eVocê ainda tem " + time + (time == 1 ? " Minuto" : " Minutos") + " para coletar blocos"));
                    return true;
                } else {
                    player.sendMessage(getPmTTC("&cVocê não tem o bcollect comprado"));
                }
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("comprar")) {
                if (!collectBlock.containsKey(player.getName())) {
//                    Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
                    if (true) {
//                        economy.withdrawPlayer(player.getName(), 5000);
                        collectBlock.put(player.getName(), 10);
                        player.sendMessage(getPmTTC("&aVocê comprou 10 Minutos de coletor de blocos por 5000 de money"));
                    } else {
                        player.sendMessage(getPmTTC("&cÉ meu camarada, tu precisa de 5000 Conto para comprar esta parada"));
                    }
                } else {
                    player.sendMessage(getPmTTC("&cVocê já está com o bcollect comprado"));
                }
            }
        }
        return true;
    }
}
