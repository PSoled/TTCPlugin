package com.totalcraft.soled;

import com.totalcraft.soled.Configs.InitializeConfigs;
import com.totalcraft.soled.Listeners.EventManager;
import com.totalcraft.soled.Tasks.TaskManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static com.totalcraft.soled.Commands.CommandManager.registerCommands;
import static com.totalcraft.soled.Configs.FilterBlockData.saveFilterBlock;
import static com.totalcraft.soled.Configs.ItemPrivateLog.saveLogItem;
import static com.totalcraft.soled.Crates.CrateConfig.saveItemsCrate;
import static com.totalcraft.soled.PlayTIme.PlayTimeData.savePlayTime;
import static com.totalcraft.soled.PlayerManager.DataSave.SavePDataBase;
import static com.totalcraft.soled.Utils.CollectBlocksUtils.reimbursementMoney;
import static com.totalcraft.soled.auction.ConfigAuction.saveAuction;

public class Main extends JavaPlugin implements Listener {
    private static Main main;
    public static Economy economy;
    //    public static JDA botTotalCraft, botTest;
    InitializeConfigs initializeConfigs = new InitializeConfigs(this);
    TaskManager taskManager = new TaskManager();

    @Override
    public void onLoad() {
        initializeConfigs.onLoad();
    }

    @Override
    public void onEnable() {
        main = this;
        initializeConfigs.onEnable();
        registerCommands();
        taskManager.InitializeTasks();
        EventManager.registerAll(this);
        if (Bukkit.getServicesManager().getRegistration(Economy.class) != null)
            economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    }

    @Override
    public void onDisable() {
        TaskManager.TaskCancel();
        savePlayTime();
        saveAuction();
        if (economy != null) reimbursementMoney();
        saveFilterBlock();
        saveLogItem();
        SavePDataBase();
        saveItemsCrate();
    }

    public static Main get() {
        return main;
    }
}

