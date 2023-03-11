package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Configs.JailData;
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
            String playerName = args[0].toLowerCase();
            Player playerJail = Bukkit.getPlayer(playerName);

            if (!JailData.jailListPlayer.containsKey(playerName)) {
                sender.sendMessage(getPmTTC("&cEste Player não está preso!"));
                return true;
            }

            if (playerJail != null) {
                playerJail.teleport(new Location(Bukkit.getWorld("spawn"),0, 65 ,0));
            }
            JailData.jailConfig.set(playerName, null);
            JailData.jailListPlayer.remove(playerName);
            PermissionUser userJail = PermissionsEx.getPermissionManager().getUser(playerJail);
            userJail.setGroups(new String[]{"Civil"});
            Bukkit.broadcastMessage(getPmTTC(playerName + " &cfoi retirado da prisão"));
            JailData.saveJailData();

        }
        return true;
    }
}
