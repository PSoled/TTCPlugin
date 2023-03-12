package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class Utils {

    public static boolean getAdm(CommandSender sender) {
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        return !user.has("ttcsoled.admin") && !sender.isOp();
    }

    public static boolean cancelTpStaff(String command, Player player) {
        String[] commandParts = command.split(" ");
        if (commandParts.length >= 2 && commandParts[0].equalsIgnoreCase("/tp")) {
            PermissionUser user = PermissionsEx.getUser(player);
            if (user.has("totalessentials.commands.tp")) {
                Player target = Bukkit.getPlayer(commandParts[1]);
                if (target != null && (target.getName().equalsIgnoreCase("PlayerSoled") || target.getName().equalsIgnoreCase("Ythan") || target.getName().equalsIgnoreCase("Gilberto"))) {
                    if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        if (!user.has("ttcsoled.tpadmin") && !player.isOp()) {
                            player.sendMessage(getPmTTC("&cSeu superior está no vanish, meu chará"));
                            target.sendMessage(getPmTTC(player.getName() + " &cTentou teleportar em você, mas você está de vanish"));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
