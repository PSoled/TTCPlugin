package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Utils.RestartServerUtils;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class CommandsPlugin implements CommandExecutor {
    private final PluginManager pluginManager;
    private final RestartServerUtils restartServerUtils;

    public CommandsPlugin(PluginManager pluginManager, Plugin plugin) {
        this.pluginManager = pluginManager;
        restartServerUtils = new RestartServerUtils(plugin);
    }
    Utils utils = new Utils();


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
                case "som":
                case "teste":
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

            if (args.length > 0 && args[0].equalsIgnoreCase("som")) {
                if (args.length != 3) {
                    sender.sendMessage(getPmTTC("&cUse: /ttcsoled som <Player> <Som>"));
                    return true;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(getPmTTC("&cEste Player não está online!"));
                    return true;
                }
                try {
                    Sound som = Sound.valueOf(args[2]);
                    player.playSound(player.getLocation(), som, 1, 1);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(getPmTTC("&cO som &b" + args[2] + " &cnão existe no Minecraft."));
                }
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("teste")) {
                Bukkit.reload();
            }
        }
        return true;
    }
}

