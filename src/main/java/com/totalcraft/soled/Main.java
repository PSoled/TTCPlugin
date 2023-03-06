package com.totalcraft.soled;

import com.totalcraft.soled.Commands.*;
import com.totalcraft.soled.Configs.*;
import com.totalcraft.soled.Listeners.EventManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onLoad() {
        MainConfig mainConfig = new MainConfig(this);
        mainConfig.initializeConfigs();
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
        getCommand("vender").setExecutor(new Vender());
        getCommand("blockprotect").setExecutor(new BlockProtect());

        ConsoleFilter.register(getLogger());
        BlockProtectData blockProtectData = new BlockProtectData(this);
        blockProtectData.loadProtectedBlocks();
        Jail.jailTime();
        Bfly.bflyTime();
        EventManager.registerAll(this);
    }

    @Override
    public void onDisable() {
        Bfly.cancelBflyTime();
        Jail.canceljailTime();
        BflyData.saveFlyData();
        JailData.saveJailData();
    }
}

