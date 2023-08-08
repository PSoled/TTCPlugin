package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class SetHome implements CommandExecutor {
    public static List<String> questionHome = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return true;
        Map<String, Location> homes = playerBase.getHomes();
        Location loc = player.getLocation();
        String home = args.length > 0 ? args[0] : "home";
        if (homes.containsKey(home)) {
            player.sendMessage(getPmTTC("&cJá existe uma home com este nome\n&eSe deseja sobrescrever ela escreva &aSim ou Não"));
            questionHome.add(player.getName());
            playerBase.setCacheHomeName(home);
            playerBase.setCacheHomeLoc(loc);
        } else {
            player.sendMessage(getPmTTC("&aHome setada com Sucesso!"));
            playerBase.addHome(home, loc);
            playerBase.saveData();
        }
        return true;
    }
}
