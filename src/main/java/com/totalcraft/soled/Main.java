package com.totalcraft.soled;

import com.totalcraft.soled.Commands.CommandManager;
import com.totalcraft.soled.Configs.InitializeConfigs;
import com.totalcraft.soled.Listeners.EventManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    InitializeConfigs initializeConfigs = new InitializeConfigs(this);
    CommandManager commandManager = new CommandManager(this);
    TaskManager taskManager = new TaskManager(this);

    @Override
    public void onLoad() {
        initializeConfigs.onLoad();
    }

    @Override
    public void onEnable() {
        commandManager.registerCommands();
        initializeConfigs.onEnable();
        taskManager.TaskDebug();
        EventManager.registerAll(this);
    }

    @Override
    public void onDisable() {
        taskManager.TaskCancel();
    }
}

