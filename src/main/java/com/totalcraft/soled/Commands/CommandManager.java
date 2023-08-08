package com.totalcraft.soled.Commands;

import com.totalcraft.soled.Main;

public class CommandManager {
    public static void registerCommands() {
        Main.get().getCommand("ttcsoled").setExecutor(new CommandsPlugin(Main.get()));
        Main.get().getCommand("rankup").setExecutor(new Rankup());
        Main.get().getCommand("tagrank").setExecutor(new TagRank());
        Main.get().getCommand("mina").setExecutor(new EventoMina(Main.get()));
        Main.get().getCommand("module").setExecutor(new Modules());
        Main.get().getCommand("sfly").setExecutor(new Bfly());
        Main.get().getCommand("jail").setExecutor(new Jail());
        Main.get().getCommand("unjail").setExecutor(new UnJail());
        Main.get().getCommand("vender").setExecutor(new Vender());
        Main.get().getCommand("blockprotect").setExecutor(new BlockProtect());
        Main.get().getCommand("bcollect").setExecutor(new CollectBlocks());
        Main.get().getCommand("rtp").setExecutor(new RandomTp());
        Main.get().getCommand("banitem").setExecutor(new BanItem());
        Main.get().getCommand("blocklimit").setExecutor(new BlockLimit());
        Main.get().getCommand("filterblock").setExecutor(new FilterBlock());
        Main.get().getCommand("moises").setExecutor(new Moises());
        Main.get().getCommand("clearchunk").setExecutor(new ClearChunk());
        Main.get().getCommand("leilao").setExecutor(new Leilao());
        Main.get().getCommand("stps").setExecutor(new Tps());
        Main.get().getCommand("repair").setExecutor(new Repair());
        Main.get().getCommand("valores").setExecutor(new MinValues());
        Main.get().getCommand("crate").setExecutor(new Crate());
        Main.get().getCommand("keys").setExecutor(new Keys());
        Main.get().getCommand("autofeed").setExecutor(new AutoFeed());
        Main.get().getCommand("criarcomando").setExecutor(new NewCommand());
        Main.get().getCommand("tempoafk").setExecutor(new TempoAfk());
        Main.get().getCommand("setshop").setExecutor(new SetShop());
        Main.get().getCommand("ptsoled").setExecutor(new PTSoled());
        Main.get().getCommand("recompensa").setExecutor(new Recompensa());
        Main.get().getCommand("quarry").setExecutor(new Quarry());
//        Main.get().getCommand("sethome").setExecutor(new SetHome());
//        Main.get().getCommand("home").setExecutor(new Home());
//        Main.get().getCommand("homes").setExecutor(new Home());
//        Main.get().getCommand("delhome").setExecutor(new DelHome());
    }
}
