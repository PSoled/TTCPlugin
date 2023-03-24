package com.totalcraft.soled.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

import static com.totalcraft.soled.Configs.FilterBlockData.redBlock;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class FilterBlock implements CommandExecutor {
    public static HashMap<String, Inventory> filterChest;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        if (args.length > 0 && args[0].equalsIgnoreCase("info")) {
            player.sendMessage(getPmTTC("&bInformações sobre o filtro de bloco:" +
                    "\n&9Todos blocos minerados que não estiver dentro do filtro de blocos, serão deletados." +
                    "\n&9Caso o filtro de bloco esteja ativado e não tenha blocos, serão deletado Terra, Pedra e Gravel"));
            return true;
        }
        player.sendMessage(getPmTTC("&bAbrindo seu Filtro de Blocos"));
        Inventory chest;
        if (filterChest.get(player.getName()) == null) {
            chest = Bukkit.createInventory(null, 18, ChatColor.WHITE + "Filtro: " + ChatColor.BLACK + player.getName());
            chest.setItem(17, redBlock);
            filterChest.put(player.getName(), chest);
        } else {
            chest = filterChest.get(player.getName());
        }
        player.openInventory(chest);
        return true;
    }
}