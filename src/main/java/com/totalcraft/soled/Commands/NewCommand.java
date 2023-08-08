package com.totalcraft.soled.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmNotAdm;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class NewCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (getAdm(sender)) {
            sender.sendMessage(getPmNotAdm());
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("criarcomando")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Uso incorreto. Uso: /criarcomando <comando> <invetory>");
                return true;
            }
            String command = args[0];
            String inventory = args[1];
            Command newCommand = new Command(command) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(getPmConsole());
                        return true;
                    }
                    Player player = (Player) sender;
                    player.openInventory(getInventory(inventory));
                    return true;
                }
            };
            PluginManager pluginManager = Bukkit.getPluginManager();
            Object commandMap;
            try {
                Field commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = commandMapField.get(pluginManager);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
            ((CommandMap) commandMap).register(command, newCommand);
            sender.sendMessage(ChatColor.GREEN + "Comando " + command + " criado com sucesso.");
        }
        return true;
    }
    private Inventory getInventory(String name) {
        if (name.equalsIgnoreCase("guivips")) {
            return Bukkit.createInventory(null, 27, "VIPs");
        } else {
            return null;
        }
    }
}

