package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class DelHome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return true;
        if (args.length == 0) {
            player.sendMessage(getPmTTC("&cUse /delhome <nome>"));
            return true;
        }
        Map<String, Location> homes = playerBase.getHomes();
        if (!homes.containsKey(args[0])) {
            player.sendMessage(getPmTTC("&cVocê não tem uma home com este nome."));
            return true;
        }
        playerBase.removeHome(args[0]);
        playerBase.saveData();
        player.sendMessage(getPmTTC("&aRemovido home com Sucesso!"));
        return true;
    }

}
