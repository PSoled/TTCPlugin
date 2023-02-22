package com.totalcraft.soled;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Utils {

    public boolean getAdm(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PermissionUser user = PermissionsEx.getUser(player);
            if (user.has("ttcplugin.admin") || sender.isOp()) {
                return false;
            }
        }
        return !(sender instanceof ConsoleCommandSender);
    }
}
