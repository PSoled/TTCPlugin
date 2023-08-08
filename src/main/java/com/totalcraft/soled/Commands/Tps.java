package com.totalcraft.soled.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.totalcraft.soled.Tasks.TpsServer.*;
import static com.totalcraft.soled.Utils.PrefixMsgs.*;
import static com.totalcraft.soled.Utils.Utils.getAdm;
import static net.minecraft.server.MinecraftServer.currentTPS;

public class Tps implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }
        }
        double num = currentTPS;
        if (num > 20) {
            num = 20;
        }
        String numFormat = String.format("%.1f", num);
        sender.sendMessage("§eTPS: current, 5s, 1m, 5m, 15m, 30m");
        sender.sendMessage(getTpsFormat(Double.parseDouble(numFormat)) + "  " + getTpsFormat(TPS_5SEG) + "  " + getTpsFormat(TPS_1MIN) + "  " + getTpsFormat(TPS_5MIN) + "  " + getTpsFormat(TPS_15MIN) + "  " + getTpsFormat(TPS_30MIN));
        return true;
    }

    private String getTpsFormat(double num) {
        if (num > 16) {
            return "§a" + num;
        }
        if (num > 8) {
            return "§6" + num;
        }
        if (num >= 0) {
            return "§c" + num;
        }
        return null;
    }
}
