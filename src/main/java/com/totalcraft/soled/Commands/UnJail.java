package com.totalcraft.soled.Commands;

import com.totalcraft.soled.PlayerManager.PlayerBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmNotAdm;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class UnJail implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("unjail")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);
                if (!user.has("ttcsoled.jail") && !player.isOp()) {
                    sender.sendMessage(getPmNotAdm());
                    return true;
                }
            }
            if (args.length != 1) {
                sender.sendMessage(getPmTTC("&cUse: /unjail <Player>"));
                return true;
            }
            PlayerBase playerBase = PlayerBase.getPlayerBase(args[0]);
            if (playerBase == null) {
                sender.sendMessage(getPmTTC("&cEste player nunca entrou no servidor."));
                return true;
            }
            Player playerJail = Bukkit.getPlayer(args[0]);
            if (!playerBase.Jail) {
                sender.sendMessage(getPmTTC("&cEste Player não está preso!"));
                return true;
            }
            if (playerJail != null) {
                playerJail.teleport(new Location(Bukkit.getWorld("world"), 0, 65, 0));
            }
            playerBase.setJailTime(0);
            if (playerJail != null) {
                playerBase.Jail = false;
                PermissionUser user = PermissionsEx.getPermissionManager().getUser(playerJail);
                user.setGroups(new String[]{"Civil"});
            }
            playerBase.saveData();
            Bukkit.broadcastMessage(getPmTTC(playerBase.getName() + " &cfoi retirado da prisão"));
        }
        return true;
    }
}
