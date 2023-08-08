package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.Jail;
import com.totalcraft.soled.PlayerManager.PlayerBase;
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
import static com.totalcraft.soled.Configs.MainConfig.eventGroupChangeModule;
import static com.totalcraft.soled.Listeners.PlayerQuit.playerQuitMina;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static org.bukkit.Sound.NOTE_PLING;

public class PlayerJoin implements Listener {
    private final Plugin plugin;

    public PlayerJoin(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void JoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PermissionUser user = PermissionsEx.getUser(event.getPlayer());
        if (!user.has("ttcsoled.admin")) {
            for (Player on : Bukkit.getOnlinePlayers()) {
                on.playSound(player.getLocation(), NOTE_PLING, 1, 1);
            }
        }
    }

    RankupUtils rankupUtils = new RankupUtils();

    @EventHandler
    public void ModuleEvent(PlayerJoinEvent event) {
        if (eventGroupChangeModule) {
            String playerName = event.getPlayer().getName();
            rankupUtils.eventSetRank(playerName);
        }
    }

    @EventHandler
    public void eventMinaEvent(PlayerJoinEvent event) {
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
    }

    @EventHandler
    public void jailEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
        if (playerBase == null) return;
        if (playerBase.Jail) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(plugin, () -> {
                int time = playerBase.getJailTime();
                if (playerBase.Jail && time == 0) {
                    PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
                    user.setGroups(new String[]{"Civil"});
                    playerBase.Jail = false;
                    playerBase.saveData();
                }
                if (playerBase.Jail) {
                    player.teleport(Jail.locationJail);
                    PermissionUser userJail = PermissionsEx.getPermissionManager().getUser(player);
                    userJail.setGroups(new String[]{"Prisoners"});
                    player.sendMessage(getPmTTC("&cVocê deve ainda " + time + " Minutos de pena"));
                }
            }, 40L);
        }
    }

    @EventHandler
    public void notifyReward(PlayerJoinEvent event) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            Player player = event.getPlayer();
            PlayerBase playerBase = PlayerBase.getPlayerBase(player.getName());
            if (playerBase != null && player.isOnline()) {
                if (playerBase.getItemsGive().size() > 0) {
                    player.sendMessage("");
                    player.sendMessage(getPmTTC("&aVocê tem itens para recolher no &f/recompensa"));
                    player.sendMessage("");
                }
            }
        }, 80L);
    }
}

