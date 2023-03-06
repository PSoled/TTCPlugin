package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.EventoMina;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Sound.NOTE_BASS_DRUM;
public class PlayerQuit implements Listener {
    public static List<String> playerQuitMina = new ArrayList<>();
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PermissionUser user = PermissionsEx.getUser(event.getPlayer());
        if (!user.has("ttcsoled.admin")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), NOTE_BASS_DRUM, 1, 1);
            }
        }
        String playerName = event.getPlayer().getName();
        if (EventoMina.minaPlayers.contains(playerName)) {
            playerQuitMina.add(playerName);
            EventoMina.minaPlayers.remove(playerName);
        }
    }
}
