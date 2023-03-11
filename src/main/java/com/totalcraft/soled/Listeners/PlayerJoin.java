package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.Jail;
import com.totalcraft.soled.Utils.RankupUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import static com.totalcraft.soled.Commands.EventoMina.pickaxe;
import static com.totalcraft.soled.Configs.JailData.jailListPlayer;
import static com.totalcraft.soled.Configs.MainConfig.eventGroupChangeModule;
import static com.totalcraft.soled.Listeners.PlayerQuit.playerQuitMina;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static org.bukkit.Sound.NOTE_PLING;

public class PlayerJoin implements Listener {
    private final Plugin plugin;

    public PlayerJoin(Plugin plugin) {
        this.plugin = plugin;
    }
    RankupUtils rankupUtils = new RankupUtils();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PermissionUser user = PermissionsEx.getUser(event.getPlayer());
        if (!user.has("ttcsoled.admin")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), NOTE_PLING, 1, 1);
            }
        }
        if (eventGroupChangeModule) {
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

        if (jailListPlayer.containsKey(playerName)) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                Player player = event.getPlayer();
                player.teleport(Jail.locationJail);
                int time = jailListPlayer.get(playerName);
                player.sendMessage(getPmTTC("&cVocê deve ainda " + time + " Horas de pena"));
            }, 40L);
        }
    }
}
