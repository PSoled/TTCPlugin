package com.totalcraft.soled.Commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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
    public static HashMap<String, Integer> BlockFilter = new HashMap<>();
    public static List<Integer> oresFilter = Arrays.asList(4, 15, 16, 2762, 24, 3, 12, 249, 4362, 2402, 2001, 13, 1750, 14, 351, 331, 264, 388, 30243, 248, 4329, 3346, 4321, 4322, 4323, 4324, 4325, 4326, 4327, 4328, 714, 4330, 4345, 1475, 2100, 25264, 6278, 6319, 6320, 25263, 263, 318, 280, 27972);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
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
                    Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
                    if (economy.has(player.getName(), 5000)) {
                        economy.withdrawPlayer(player.getName(), 5000);
                        collectBlock.put(player.getName(), 10);
                        player.sendMessage(getPmTTC("&aVocê comprou 10 Minutos de coletor de blocos por 5000 de money"));
                    } else {
                        player.sendMessage(getPmTTC("&cÉ meu camarada, tu precisa de 5000 Conto para comprar esta parada"));
                    }
                } else {
                    player.sendMessage(getPmTTC("&cVocê já está com o bcollect comprado"));
                }
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("filtro")) {
                if (args.length > 1 && args[1].equalsIgnoreCase("clear")) {
                    player.sendMessage(getPmTTC("&eVocê limpou seu filtro de itens"));
                    BlockFilter.remove(player.getName());
                    return true;
                }
                if (args.length != 2) {
                    player.sendMessage(getPmTTC("&cUso /bcollect filtro clear"));
                    player.sendMessage(getPmTTC("&cUso /bcollect filtro <Id>"));
                    return true;
                }
                int id;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException a) {
                    sender.sendMessage(getPmTTC("&cVocê deve colocar somente o ID do item, Exemplo:\n&eId e MetaData do Dark silicon é 2100:1, Você colocar apenas 2100"));
                    return true;
                }
                if (id == 0) {
                    player.sendMessage(getPmTTC("&cSinto-lhe informar que não existe uma forma de você coletar o vento :/"));
                    return true;
                }
                player.sendMessage(getPmTTC("&eVocê filtrou seu coletor para todo bloco de ID " + id));
                BlockFilter.put(player.getName(), id);
            }
        }
        return true;
    }
}
