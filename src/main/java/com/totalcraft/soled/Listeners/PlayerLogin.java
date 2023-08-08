package com.totalcraft.soled.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.totalcraft.soled.Configs.AddressLog.saveAddressLog;
import static com.totalcraft.soled.Utils.Utils.*;

public class PlayerLogin implements Listener {

//    @EventHandler
//    public void onPlayerLogin(PlayerPreLoginEvent event) {
//        String name = event.getName();
//        for (Player online : Bukkit.getOnlinePlayers()) {
//            if (online.getName().equals(name)) {
//                event.setKickMessage("abc");
//            }
//        }
//    }

    @EventHandler
    public void getAddressEvent(PlayerLoginEvent event) throws Exception {
        Player player = event.getPlayer();
        String ip = event.getAddress().getHostAddress();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!getAdm(online)) {
                online.sendMessage(getAddressPlayerMsg(ip));
            }
        }
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar cal = Calendar.getInstance(tz);
        Date date = cal.getTime();
        String regions = null;
        String dateFormat = date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " - " + player.getName();
        try {
            regions = getAddressPlayer(ip);
        } catch (Exception ignored) {
            System.out.println("Erro ao pegar ip");
        }
        if (regions != null) {
            String[] part = regions.split("-");
            String address = "IP: " + ip + " País: " + part[0] + " Estado: " + part[1] + " Cidade: " + part[2];
            saveAddressLog(dateFormat, address);
//            TextChannel channel = botTotalCraft.getTextChannelById("827412052623884288");
//            assert channel != null;
//            channel.sendMessage("Player: " + player.getName() + " " + address).queue();
        }
    }
}

