package com.totalcraft.soled;

import com.totalcraft.soled.Commands.*;
import com.totalcraft.soled.Configs.BflyData;
import com.totalcraft.soled.Configs.JailData;
import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Listeners.Events;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onLoad() {
        MainConfig mainConfig = new MainConfig(this);
        mainConfig.setConfigs();
        BflyData bflySave = new BflyData(this);
        bflySave.loadFlyData();
        JailData jailData = new JailData(this);
        jailData.loadJailData();
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        CommandsPlugin commands = new CommandsPlugin(pluginManager);
        getCommand("ttcsoled").setExecutor(commands);

        getCommand("rankup").setExecutor(new Rankup());
        getCommand("tagrank").setExecutor(new TagRank());
        getCommand("mina").setExecutor(new EventoMina(this));
        getCommand("module").setExecutor(new Modules());
        getCommand("sfly").setExecutor(new Bfly());
        getCommand("jail").setExecutor(new Jail());
        getCommand("unjail").setExecutor(new UnJail());

        Jail.jailTime();
        Bfly.bflyTime();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Events(this), this);
        Events events = new Events(this);
        Bukkit.getPluginManager().registerEvents(events, this);
        events.registerEvents(this);
    }

    @Override
    public void onDisable() {
        Bfly.cancelBflyTime();
        Jail.canceljailTime();
        BflyData.saveFlyData();
        JailData.saveJailData();
    }
}

