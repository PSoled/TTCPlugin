package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class Jail implements CommandExecutor {
    Utils utils = new Utils();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("jail")) {
            if (sender instanceof Player) {
                if (utils.getAdm(sender)) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
        }
        return true;
    }
}
