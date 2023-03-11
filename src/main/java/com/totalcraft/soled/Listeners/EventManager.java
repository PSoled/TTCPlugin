package com.totalcraft.soled.Listeners;

import com.totalcraft.soled.Main;
import org.bukkit.plugin.PluginManager;

public class EventManager {
    public static void registerAll(Main plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(plugin), plugin);
        pm.registerEvents(new PlayerQuit(), plugin);
        pm.registerEvents(new GroupChange(), plugin);
        pm.registerEvents(new PlayerCommandPreprocess(), plugin);
        pm.registerEvents(new Teleport(), plugin);
        pm.registerEvents(new ChangedWorld(plugin), plugin);
        pm.registerEvents(new BlockPlace(), plugin);
        pm.registerEvents(new BlockBreak(), plugin);
        pm.registerEvents(new PlayerInteract(), plugin);
        pm.registerEvents(new PlayerToggleFlight(), plugin);
        pm.registerEvents(new InventoryClose(), plugin);
        pm.registerEvents(new PlayerItemHeld(), plugin);
        pm.registerEvents(new InventoryClick(), plugin);
        pm.registerEvents(new InventoryDrag(), plugin);
        pm.registerEvents(new PlayerDropItem(), plugin);
        pm.registerEvents(new PlayerPickupItem(plugin), plugin);
    }
}