package com.totalcraft.soled.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerCommandPreprocess implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        String[] commandParts = command.split(" ");
        if (commandParts.length >= 2 && commandParts[0].equalsIgnoreCase("/tp")) {
            PermissionUser user = PermissionsEx.getUser(event.getPlayer());
            if (user.has("totalessentials.commands.tp")) {
                Player target = Bukkit.getPlayer(commandParts[1]);
                if (target != null && (target.getName().equalsIgnoreCase("PlayerSoled") || target.getName().equalsIgnoreCase("Ythan") || target.getName().equalsIgnoreCase("Gilberto"))) {
                    if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        if (!user.has("ttcsoled.tpadmin") && !event.getPlayer().isOp()) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(getPmTTC("&cSeu superior está no vanish, meu chará"));
                            target.sendMessage(getPmTTC(event.getPlayer().getName() + " &cTentou teleportar em você, mas você está de vanish"));
                        }
                    }
                }
            }
        }
    }
}
