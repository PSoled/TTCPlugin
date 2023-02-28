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
    Utils utils = new Utils();

    public static String rankup = "Comando";
    public static String groupchange = "Evento";

    private static MainConfig mainInstance;

    public static void setMainInstance(MainConfig mainInstance) {
        Modules.mainInstance = mainInstance;
    }

    private CommandSender sender;

    private final Map<String, Runnable> moduleFunctions = new HashMap<String, Runnable>() {{
        put("rankup on", () -> {
            mainInstance.setRankupModule(true); // Usa a referência para a instância de Main
            sender.sendMessage(getPmCommandAdm(rankup, "/rankup", true));
        });
        put("rankup off", () -> {
            mainInstance.setRankupModule(false); // Usa a referência para a instância de Main
            sender.sendMessage(getPmCommandAdm(rankup, "/rankup", false));
        });
        put("groupchange on", () -> {
            mainInstance.setEventGroupChangeModule(true); // Usa a referência para a instância de Main
            sender.sendMessage(getPmCommandAdm(groupchange, "groupchange", true));
        });
        put("groupchange off", () -> {
            mainInstance.setEventGroupChangeModule(false); // Usa a referência para a instância de Main
            sender.sendMessage(getPmCommandAdm(groupchange, "groupchange", false));
        });
        put(("list"), () -> sender.sendMessage(getListModule()));
    }};

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (utils.getAdm(sender)) {
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
