package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.EventoMina;
import com.totalcraft.soled.Commands.Jail;
import com.totalcraft.soled.Configs.BflyData;
import com.totalcraft.soled.Configs.JailData;
import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.RankupUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Commands.EventoMina.pickaxe;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static org.bukkit.Sound.*;

public class Events implements Listener {
    RankupUtils rankupUtils = new RankupUtils();
    List<String> playerQuitMina = new ArrayList<>();

    private final Plugin plugin;

    public Events(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGroupChange(PermissionEntityEvent event) {
        if (MainConfig.eventGroupChangeModule) {
            if (event.getEntity() instanceof PermissionUser) {
                String playerName = event.getEntity().getName();
                rankupUtils.eventSetRank(playerName);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PermissionUser user = PermissionsEx.getUser(event.getPlayer());
        if (!user.has("ttcsoled.admin")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), NOTE_PLING, 1, 1);
            }
        }
        if (MainConfig.eventGroupChangeModule) {
            String playerName = event.getPlayer().getName();
            rankupUtils.eventSetRank(playerName);
        }
        String playerName = event.getPlayer().getName();
        if (playerQuitMina.contains(playerName)) {
            Player player = event.getPlayer();
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            PlayerInventory inventory = player.getInventory();
            inventory.remove(Material.COOKED_BEEF);
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == pickaxe.getType()) {
                    inventory.clear(i);
                }
            }
            player.sendMessage(getPmTTC("&cVocê saiu durante Evento Mina"));
        }

        if (JailData.jailListPlayer.containsKey(playerName)) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                Player player = event.getPlayer();
                player.teleport(Jail.locationJail);
                int time = JailData.jailListPlayer.get(playerName);
                player.sendMessage(getPmTTC("&cVocê deve ainda " + time + " Horas de pena"));
            }, 40L);
        }
    }


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

    @EventHandler
    public void PlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (BflyData.flyListPlayer.containsKey(name)) {
            String world = player.getWorld().getName();
            if (!world.equals("spawn")) {
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(plugin, () -> {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }, 40L);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        player.playSound(player.getLocation(), LEVEL_UP, 1, 1);
    }

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



