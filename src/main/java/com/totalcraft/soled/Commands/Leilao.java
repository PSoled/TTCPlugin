package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.MainConfig;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.auction.Hub.hubAuction;

public class Leilao implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        if (!MainConfig.leilaoModule) {
            sender.sendMessage(getPmTTC("&cLeilão Desativado."));
            return true;
        }
        Player player = (Player) sender;
        player.openInventory(hubAuction);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        return true;
    }
}
