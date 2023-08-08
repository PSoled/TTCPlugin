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
import static com.totalcraft.soled.Utils.Utils.getAdm;

public class Home implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        Player player = (Player) sender;
        if (!getAdm(player)) {
            if (args.length > 0 && args[0].contains(":")) {
                String[] split = args[0].split(":");
                if (split.length == 1) {
                    PlayerBase target = PlayerBase.getPlayerBase(split[0]);
                    if (target == null) {
                        player.sendMessage(getPmTTC("&cEste player nunca entrou no servidor."));
                        return true;
                    }
                    player.sendMessage(getPmTTC("&bLista de homes de &f" + target.getName()));
                    player.sendMessage("§e" + target.getHomes().keySet());
                    return true;
                } else {
                    PlayerBase target = PlayerBase.getPlayerBase(split[0]);
                    if (target == null) {
                        player.sendMessage(getPmTTC("&cEste player nunca entrou no servidor."));
                        return true;
                    }
                    if (!target.getHomes().containsKey(split[1])) {
                        player.sendMessage(getPmTTC("&cO Player alvo não contém esta home."));
                        return true;
                    }
                    player.teleport(target.getHomes().get(split[1]));
                    player.sendMessage(getPmTTC("&aTeleportado para a home de " + target.getName()));
                    return true;
                }
            }
        }
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return true;
        Map<String, Location> homes = playerBase.getHomes();
        if (args.length == 0) {
            if (homes.size() == 0) {
                player.sendMessage(getPmTTC("&cVocê não tem nenhuma home setada."));
                return true;
            }
            player.sendMessage(getPmTTC("&bSuas homes setadas"));
            player.sendMessage("§e" + homes.keySet());
        } else {
            if (!homes.containsKey(args[0])) {
                player.sendMessage(getPmTTC("&cVocê não tem uma home com este nome."));
                return true;
            }
            player.teleport(homes.get(args[0]));
            player.sendMessage(getPmTTC("&aTeleportado para sua home."));
        }

        return true;
    }
}
