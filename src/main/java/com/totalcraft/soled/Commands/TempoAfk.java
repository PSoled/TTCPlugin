package com.totalcraft.soled.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Tasks.AfkPlayer.timeAfk;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class TempoAfk implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmTTC("&cUse /tempoafk (nick)"));
                return true;
            }
            Player player = (Player) sender;
            if (timeAfk.containsKey(player.getName())) {
                int time = timeAfk.get(player.getName());
                player.sendMessage(getPmTTC("&bSeu tempo afk é de &f" + time + " &bMinutos"));
            } else {
                player.sendMessage(getPmTTC("&cParece que houve um problema"));
            }
            return true;
        }
        String player = args[0];
        for (String nick : timeAfk.keySet()) {
            if (nick.equalsIgnoreCase(player)) {
                int time = timeAfk.get(nick);
                sender.sendMessage(getPmTTC("&bO tempo afk deste player é de &f" + time + " &bMinutos"));
                return true;
            }
        }
        sender.sendMessage(getPmTTC("&cParece que este player não está online"));
        return true;
    }
}
