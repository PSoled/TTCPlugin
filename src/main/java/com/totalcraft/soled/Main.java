package com.totalcraft.soled;

import com.totalcraft.soled.Commands.CommandManager;
import com.totalcraft.soled.Configs.InitializeConfigs;
import com.totalcraft.soled.Listeners.EventManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static com.totalcraft.soled.Configs.FilterBlockData.saveFilterBlock;
import static com.totalcraft.soled.Utils.CollectBlocksUtils.reimbursementMoney;

public class Main extends JavaPlugin implements Listener {
    InitializeConfigs initializeConfigs = new InitializeConfigs(this);
    CommandManager commandManager = new CommandManager(this);

    @Override
    public void onLoad() {
        initializeConfigs.onLoad();
    }

    @Override
    public void onEnable() {
        commandManager.registerCommands();
        initializeConfigs.onEnable();
        TaskManager.InitializeTasks();
        EventManager.registerAll(this);
    }

    @Override
    public void onDisable() {
        TaskManager.TaskCancel();
        reimbursementMoney();
        saveFilterBlock();
    }

}

