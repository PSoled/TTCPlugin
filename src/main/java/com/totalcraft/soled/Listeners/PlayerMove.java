package com.totalcraft.soled.Listeners;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Utils.GriefPreventionUtils.hasPermEntry;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                Block delete = world.getBlockAt((int) loca.getX(), (int) (loca.getY() - 1), (int) loca.getZ());
//                delete.setType(Material.AIR);
//            }
//        }.runTaskLater(Main.get(), 20);


        if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getTo(), false, null);
            if (claim != null) {
                PermissionUser user = PermissionsEx.getUser(player);
                if (!user.has("ttcsoled.bypass.entrytrust") && !player.isOp()) {
                    if (!claim.ownerName.equals(player.getName())) {
                        if (hasPermEntry(player, claim)) {
                            Location loc = claim.getGreaterBoundaryCorner();
                            Location newLoc = new Location(player.getWorld(), loc.getX() + 2, player.getLocation().getY(), loc.getZ() + 2);
                            player.teleport(newLoc);
                            event.getPlayer().sendMessage(getPmTTC("&cVocê não tem permissão para entrar neste terreno."));
                        }
                    }
                }
            }
        }
    }
}


