package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Utils.GriefPreventionUtils;
import com.totalcraft.soled.Utils.Utils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

import static com.totalcraft.soled.Utils.GriefPreventionUtils.hasBlock;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.Utils.Utils.cancelTpStaff;

public class PlayerCommandPreprocess implements Listener {
    private final Plugin plugin;

    public PlayerCommandPreprocess(Plugin plugin) {
        this.plugin = plugin;
    }
    public static HashMap<String, Boolean> msgAgain = new HashMap<>();
    public static HashMap<String, Integer> cooldownCmd = new HashMap<>();
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (cancelTpStaff(command, player)) {
            event.setCancelled(true);
        }
        if (command.startsWith("/abandonclaim") || command.startsWith("/abandonallclaims")) {
            if (cooldownCmd.containsKey(player.getName())) {
                player.sendMessage(getPmTTC("&cVocê deve aguadar " + cooldownCmd.get(player.getName()) + " Segundos para digitar este comando"));
                event.setCancelled(true);
                return;
            }
        }
        if (Utils.getAdm(player)) {
            GriefPreventionUtils griefPreventionUtils = new GriefPreventionUtils(plugin);
            if (command.startsWith("/abandonclaim")) {
                cooldownCmd.put(player.getName(), 15);
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null);
                int delay = griefPreventionUtils.claimContainsBlock(player, claim, world, false, false, true);
                event.setCancelled(true);
                player.sendMessage("\n" + getPmTTC("&c&lVerificando o terreno, Aguarde&r") + "\n ");
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (!hasBlock.get(player.getName())) {
                        player.performCommand("abandonclaim");
                    }
                }, 10L * delay);
            }
            if (command.startsWith("/abandonallclaims")) {
                msgAgain.put(player.getName(), false);
                cooldownCmd.put(player.getName(), 15);
                List<Claim> claims = GriefPrevention.instance.dataStore.getPlayerData(player.getName()).claims;
                for (Claim claim : claims) {
                    int delay = griefPreventionUtils.claimContainsBlock(player, claim, world, true, false, true);
                    event.setCancelled(true);
                    if (!msgAgain.get(player.getName())) {
                        player.sendMessage("\n " + getPmTTC("&c&lVerificando seus terrenos, Aguarde&r") + "\n ");
                    }
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (!hasBlock.get(player.getName())) {
                            player.performCommand("abandonallclaims");
                        }
                    }, 10L * delay);
                    msgAgain.put(player.getName(), true);
                }
            }
        }
    }
}
