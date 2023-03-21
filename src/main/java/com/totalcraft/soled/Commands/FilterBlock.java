package com.totalcraft.soled.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class FilterBlock implements CommandExecutor {
    public static HashMap<String, Inventory> filterChest;
    public static HashMap<String, Boolean> filterBoolean;

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
                    "\n&9Caso o filtro de bloco esteja ativado e não tenha blocos, serão deletado Terra, Pedra e Gravel\n" +
                    "\n&a/filterblock on &eAtiva o Filtro de bloco" +
                    "\n&a/filterblock &coff &e... :P"));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("on")) {
            filterBoolean.put(player.getName(), true);
            player.sendMessage(getPmTTC("&fVocê &aAtivou &fo filtro de blocos"));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("off")) {
            filterBoolean.put(player.getName(), false);
            player.sendMessage(getPmTTC("&fVocê &cDesativou &fo filtro de blocos"));
            return true;
        }
        player.sendMessage(getPmTTC("&bAbrindo seu Filtro de Blocos"));
        Inventory chest;
        if (filterChest.get(player.getName()) == null) {
            chest = Bukkit.createInventory(null, 9, ChatColor.WHITE + "Filtro: " + ChatColor.BLACK + player.getName());
            filterBoolean.put(player.getName(), false);
            filterChest.put(player.getName(), chest);
        } else {
            chest = filterChest.get(player.getName());
        }
        player.openInventory(chest);
        return true;
    }
}