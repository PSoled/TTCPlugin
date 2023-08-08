package com.totalcraft.soled.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Crates.CrateBase.invHubCrate;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class Crate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        if (getAdm(sender)) {
            sender.sendMessage(getPmNotAdm());
            return true;
        }
        Player player = (Player) sender;
        player.openInventory(invHubCrate);
        return true;
    }
}
