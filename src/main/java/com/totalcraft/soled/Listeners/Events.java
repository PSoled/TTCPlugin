package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Commands.EventoMina;
import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.RankupUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Commands.EventoMina.pickaxe;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class Events implements Listener {
    RankupUtils rankupUtils = new RankupUtils();
    List<String> playerQuitMina = new ArrayList<>();

    public void registerEvents(Plugin plugin) {
        EventoMina eventoMina = new EventoMina(plugin);
        Bukkit.getPluginManager().registerEvents(eventoMina, plugin);
        Bukkit.getServer().getPluginManager().registerEvents(eventoMina, plugin);
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
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        if (EventoMina.minaPlayers.contains(playerName)) {
            playerQuitMina.add(playerName);
            EventoMina.minaPlayers.remove(playerName);
        }
    }
}

