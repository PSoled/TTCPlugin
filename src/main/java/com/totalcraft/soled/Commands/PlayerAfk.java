package com.totalcraft.soled.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.totalcraft.soled.Tasks.AfkPlayer.timeAfk;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerAfk implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(getPmTTC("&cUse /playerafk <nick>"));
            return true;
        }
        if (!timeAfk.containsKey(args[0])) {
            sender.sendMessage(getPmTTC("&cEste player não está Afk"));
            return true;
        }
        sender.sendMessage(getPmTTC("&aEste player está afk a &b" + timeAfk.get(args[0]) + " Minutos"));
        return true;
    }
}
