package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Main;

import static org.bukkit.Bukkit.getServer;

public class CommandManager {
    private final Main plugin;
    public CommandManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        CommandsPlugin commands = new CommandsPlugin(getServer().getPluginManager(), plugin);
        plugin.getCommand("ttcsoled").setExecutor(commands);
        plugin.getCommand("rankup").setExecutor(new Rankup());
        plugin.getCommand("tagrank").setExecutor(new TagRank());
        plugin.getCommand("mina").setExecutor(new EventoMina(plugin));
        plugin.getCommand("module").setExecutor(new Modules());
        plugin.getCommand("sfly").setExecutor(new Bfly());
        plugin.getCommand("jail").setExecutor(new Jail());
        plugin.getCommand("unjail").setExecutor(new UnJail());
        plugin.getCommand("vender").setExecutor(new Vender());
        plugin.getCommand("blockprotect").setExecutor(new BlockProtect());
        plugin.getCommand("bcollect").setExecutor(new CollectBlocks());
    }
}
