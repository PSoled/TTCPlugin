package com.totalcraft.soled;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


import static com.totalcraft.soled.Main.permissionAdm;
import static com.totalcraft.soled.prefixMsgs.*;

public class Modules extends JavaPlugin {

    public static String rankup = "Comando";
    public static String groupchange = "Evento";

    private static Main mainInstance;

    public static void setMainInstance(Main mainInstance) {
        Modules.mainInstance = mainInstance;
    }

    static class ModuleCommand implements CommandExecutor {

        private CommandSender sender;

        private final Map<String, Runnable> moduleFunctions = new HashMap<String, Runnable>() {{
            put("rankup on", () -> {
                mainInstance.setRankupModule(true); // Usa a referência para a instância de Main
                sender.sendMessage(getCommandAdm(rankup, "/rankup", true));
            });
            put("rankup off", () -> {
                mainInstance.setRankupModule(false); // Usa a referência para a instância de Main
                sender.sendMessage(getCommandAdm(rankup, "/rankup", false));
            });
            put("groupchange on", () -> {
                mainInstance.setEventGroupChangeModule(true); // Usa a referência para a instância de Main
                sender.sendMessage(getCommandAdm(groupchange, "groupchange", true));
            });
            put("groupchange off", () -> {
                mainInstance.setEventGroupChangeModule(false); // Usa a referência para a instância de Main
                sender.sendMessage(getCommandAdm(groupchange, "groupchange", false));
            });
            put(("list"), () -> {
                sender.sendMessage(getlistModule());
            });
        }};
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            Player player = (Player) sender;
            PermissionUser user = PermissionsEx.getUser(player);
            if (user.has(permissionAdm)) {
                if (cmd.getName().equalsIgnoreCase("module")) {
                    this.sender = sender;
                    if (args.length >= 1) {
                        String moduleKey = String.join(" ", args).toLowerCase(); // ou toUpperCase()
                        if (moduleFunctions.containsKey(moduleKey)) {
                            moduleFunctions.get(moduleKey).run();
                            return true;
                        }
                    }
                    sender.sendMessage(getTTCMessage("&cUtilize /module <module> on/off e /module list para ver os módulos"));
                    return true;
                }
            } else {
                sender.sendMessage(getNotAdm());
            }
            return true;
        }
    }
}