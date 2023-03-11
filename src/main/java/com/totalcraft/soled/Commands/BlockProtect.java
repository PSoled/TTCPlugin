package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.BlockProtectData;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class BlockProtect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blockprotect")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "localizar":
                case "loc":
                    break;
                default:
                    sender.sendMessage(getCommandBP(sender));
                    break;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("localizar")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(getPmConsole());
                    return true;
                }
                Player player = (Player) sender;
                if (!Objects.equals(BlockProtectData.getLocBlock(player.getName()), "null")) {
                    player.sendMessage(getPmTTC("&eLista da localização do seus blocos protegidos\n"));
                    player.sendMessage(ChatColor.AQUA + BlockProtectData.getLocBlock(player.getName()));
                    return true;
                }
                player.sendMessage(getPmTTC("&cVocê não tem blocos protegidos"));
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("loc")) {
                if (sender instanceof Player) {
                    if (Utils.getAdm(sender)) {
                        sender.sendMessage(getPmNotAdm());
                        return true;
                    }
                }
                if (args.length != 2) {
                    sender.sendMessage(getPmTTC("&cUse: /blockprotect loc <Player>"));
                    return true;
                }
                String playerName = args[1];
                if (!Objects.equals(BlockProtectData.getLocBlock(playerName), "null")) {
                    sender.sendMessage(getPmTTC("&eLista da localização dos blocos protegidos do " + playerName + "\n"));
                    sender.sendMessage(ChatColor.AQUA + BlockProtectData.getLocBlock(playerName));
                    return true;
                }
                sender.sendMessage(getPmTTC("&cEste player não tem blocos protegidos"));
            }
        }
        return true;
    }
}

