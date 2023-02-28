package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Utils.RestartServerUtils;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class CommandsPlugin implements CommandExecutor {
    private final PluginManager pluginManager;

    public CommandsPlugin(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
    Utils utils = new Utils();
    RestartServerUtils restartServerUtils = new RestartServerUtils();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ttcsoled")) {
            if (sender instanceof Player) {
                if (utils.getAdm(sender)) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "reload":
                case "restartserver":
                    break;
                default:
                    sender.sendMessage(getCommandsPlugin());
                    break;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                Plugin plugin = pluginManager.getPlugin("TTC-Soled");
                pluginManager.disablePlugin(plugin);
                pluginManager.enablePlugin(plugin);
                sender.sendMessage(getPmTTC("&f&lPlugin reniciado com Sucesso"));
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("restartserver")) {
                restartServerUtils.restartServer();
            }
            return true;
        }
        return true;
    }
}

