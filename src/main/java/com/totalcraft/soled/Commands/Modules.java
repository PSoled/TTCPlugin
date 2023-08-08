package com.totalcraft.soled.Commands;

import java.util.HashMap;
import java.util.Map;

import com.totalcraft.soled.Configs.MainConfig;
import com.totalcraft.soled.Utils.Utils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Utils.PrefixMsgs.*;

public class Modules implements CommandExecutor {

    public static String rankup,vender, rtp, bcollect, leilao;
    public static String groupchange = "Evento";

    private static MainConfig mainInstance;

    public static void setMainInstance(MainConfig mainInstance) {
        Modules.mainInstance = mainInstance;
    }

    private CommandSender sender;

    private final Map<String, Runnable> moduleFunctions = new HashMap<String, Runnable>() {{
        put("rankup on", () -> {
            mainInstance.setRankupModule(true);
            sender.sendMessage(getPmCommandAdm(rankup, "/rankup", true));
        });
        put("rankup off", () -> {
            mainInstance.setRankupModule(false);
            sender.sendMessage(getPmCommandAdm(rankup, "/rankup", false));
        });
        put("groupchange on", () -> {
            mainInstance.setEventGroupChangeModule(true);
            sender.sendMessage(getPmCommandAdm(groupchange, "groupchange", true));
        });
        put("groupchange off", () -> {
            mainInstance.setEventGroupChangeModule(false);
            sender.sendMessage(getPmCommandAdm(groupchange, "groupchange", false));
        });
        put("vender on", () -> {
            mainInstance.setVenderModule(true);
            sender.sendMessage(getPmCommandAdm(vender, "/vender", true));
        });
        put("vender off", () -> {
            mainInstance.setVenderModule(false);
            sender.sendMessage(getPmCommandAdm(vender, "/vender", false));
        });
        put("rtp on", () -> {
            mainInstance.setRtpModule(true);
            sender.sendMessage(getPmCommandAdm(rtp, "/rtp", true));
        });
        put("rtp off", () -> {
            mainInstance.setRtpModule(false);
            sender.sendMessage(getPmCommandAdm(rtp, "/rtp", false));
        });
        put("bcollect on", () -> {
            mainInstance.setBcollectModule(true);
            sender.sendMessage(getPmCommandAdm(bcollect, "/bcollect", true));
        });
        put("bcollect off", () -> {
            mainInstance.setBcollectModule(false);
            sender.sendMessage(getPmCommandAdm(bcollect, "/bcollect", false));
        });
        put("leilao on", () -> {
            mainInstance.setLeilaoModule(true);
            sender.sendMessage(getPmCommandAdm(leilao, "/leilao", true));
        });
        put("leilao off", () -> {
            mainInstance.setLeilaoModule(false);
            sender.sendMessage(getPmCommandAdm(leilao, "/leilao", false));
        });
        put(("list"), () -> sender.sendMessage(getListModule()));
    }};

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (Utils.getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("module")) {
            this.sender = sender;
            if (args.length >= 1) {
                String moduleKey = String.join(" ", args).toLowerCase(); // ou toUpperCase()
                if (moduleFunctions.containsKey(moduleKey)) {
                    moduleFunctions.get(moduleKey).run();
                    return true;
                }
            }
            sender.sendMessage(getPmTTC("&cUtilize /module <module> on/off e /module list para ver os módulos"));
            return true;
        }
        return true;
    }
}
