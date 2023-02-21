package com.totalcraft.soled;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends JavaPlugin implements Listener {


    public static String permissionAdm = "ttcplugin.admin";
    private File configFile;
    private YamlConfiguration config;
    private Modules modules;
    public static boolean rankupModule = true;
    public static boolean eventGroupChangeModule = true;
    RankupUtils rankupUtils = new RankupUtils();
    List<String> ranksList = rankupUtils.ranksList;
    List<String> listGroups = rankupUtils.listGroups;

    @Override
    public void onEnable() {
        EventoMina eventoMina = new EventoMina(this);
        getServer().getPluginManager().registerEvents(eventoMina, this);
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("rankup").setExecutor(new Rankup.RankupCommand());
        getCommand("module").setExecutor(new Modules.ModuleCommand());
        getCommand("mina").setExecutor(new EventoMina(this));

        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        config.options().header("-- Plugin TTC --\n\nModules para desativar e ativar:");

        config.addDefault("rankupModule", rankupModule);
        config.addDefault("eventGroupChangeModule", eventGroupChangeModule);

        config.options().copyDefaults(true);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        modules = new Modules();
        modules.setMainInstance(this);
    }

    @Override
    public void onDisable() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRankupModule(boolean value) {
        rankupModule = value;
        config.set("rankupModule", value);
    }

    public void setEventGroupChangeModule(boolean value) {
        eventGroupChangeModule = value;
        config.set("eventGroupChangeModule", value);
    }

    @EventHandler
    public void onGroupChange(PermissionEntityEvent event) {
        if (eventGroupChangeModule) {
            String playerName = event.getEntity().getName();
            PermissionManager permissionManager = PermissionsEx.getPermissionManager();
            PermissionUser user = permissionManager.getUser(playerName);

            if (ranksList.stream().anyMatch(user::has) && listGroups.stream().anyMatch(user::inGroup)) {
                Player player = getServer().getPlayer(playerName);
                String currentRank = rankupUtils.getCurrentRank(player, ranksList);
                String newGroup = user.getGroups()[0].getName();
                String newPermission = "ChestShop.profit." + newGroup + "." + currentRank;

                if (!user.has(newPermission)) {
                    user.addPermission(newPermission);
                    rankupUtils.removeAllPerms(player, ranksList, listGroups);
                }
            }
        }
    }
}

