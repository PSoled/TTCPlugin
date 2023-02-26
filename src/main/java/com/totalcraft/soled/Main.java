package com.totalcraft.soled;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Configs configs = new Configs(this);
        configs.setConfigs();
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("rankup").setExecutor(new Rankup());
        getCommand("tagrank").setExecutor(new TagRank());
        getCommand("mina").setExecutor(new EventoMina(this));
        getCommand("module").setExecutor(new Modules());

        getServer().getPluginManager().registerEvents(new Events(), this);
        Events events = new Events();
        Bukkit.getPluginManager().registerEvents(events, this);
        events.registerEvents(this);
    }
}

