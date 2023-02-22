package com.totalcraft.soled;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.util.List;

public class Main extends JavaPlugin implements Listener {
    private Configs configs;
    RankupUtils rankupUtils = new RankupUtils();
    List<String> ranksList = rankupUtils.ranksList;
    List<String> listGroups = rankupUtils.listGroups;

    @Override
    public void onEnable() {
        configs = new Configs(this);
        configs.setConfigs();
        EventoMina eventoMina = new EventoMina(this);
        getServer().getPluginManager().registerEvents(eventoMina, this);
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("rankup").setExecutor(new Rankup());
        getCommand("tagrank").setExecutor(new TagRank());
        getCommand("module").setExecutor(new Modules());
        getCommand("mina").setExecutor(new EventoMina(this));

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onGroupChange(PermissionEntityEvent event) {
        if (Configs.eventGroupChangeModule) {
            String playerName = event.getEntity().getName();
            rankupUtils.eventSetRank(playerName);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Configs.eventGroupChangeModule) {
            String playerName = event.getPlayer().getName();
            rankupUtils.eventSetRank(playerName);
        }
    }

}

