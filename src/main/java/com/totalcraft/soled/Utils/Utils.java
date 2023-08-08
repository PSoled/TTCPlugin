package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import java.net.*;
import java.io.*;
import org.json.*;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class Utils {

    public static boolean getAdm(CommandSender sender) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        PermissionUser user = PermissionsEx.getUser(player);
        return !user.has("ttcsoled.admin") && !sender.isOp();
    }

    public static boolean cancelTpStaff(String command, Player player) {
        String[] commandParts = command.split(" ");
        if (commandParts.length >= 2 && commandParts[0].equalsIgnoreCase("/tp")) {
            PermissionUser user = PermissionsEx.getUser(player);
            if (user.has("totalessentials.commands.tp")) {
                Player target = Bukkit.getPlayer(commandParts[1]);
                if (target != null && (target.getName().equalsIgnoreCase("PlayerSoled") || target.getName().equalsIgnoreCase("Ythan") || target.getName().equalsIgnoreCase("Gilberto"))) {
                    if (target.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                        if (!user.has("ttcsoled.tpadmin") && !player.isOp()) {
                            player.sendMessage(getPmTTC("&cSeu superior está no vanish, meu chará"));
                            target.sendMessage(getPmTTC(player.getName() + " &cTentou teleportar em você, mas você está de vanish"));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static String getAddressPlayerMsg(String ip) {
        String country = null, region = null, city = null;
        try {
            URL url = new URL("https://ipapi.co/" + ip + "/json/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            JSONObject obj = new JSONObject(content.toString());
            country = obj.getString("country_name");
            region = obj.getString("region");
            city = obj.getString("city");
        } catch (Exception ignored) {
            System.out.println("Erro ao localizar o ip: " + ip);
        }
        return getPmTTC("&bLocalização do Player Logado" +
                "\n&ePaís: &a" + country +
                "\n&eEstado: &a" + region +
                "\n&eCidade: &a" + city);
    }
    public static String getAddressPlayer(String ip) throws Exception {
        URL url = new URL("https://ipapi.co/" + ip + "/json/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        JSONObject obj = new JSONObject(content.toString());
        String country = obj.getString("country_name");
        String region = obj.getString("region");
        String city = obj.getString("city");
        return country + "-" + region + "-" + city;
    }
}
