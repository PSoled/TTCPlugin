package com.totalcraft.soled.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Utils {

    public boolean getAdm(CommandSender sender) {
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        return !user.has("ttcsoled.admin") && !sender.isOp();
    }
}
