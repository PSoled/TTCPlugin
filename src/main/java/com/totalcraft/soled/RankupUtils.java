package com.totalcraft.soled;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.events.PermissionEntityEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class RankupUtils {

    List<String> listGroups = Arrays.asList("Civil", "VipAdvanced", "VipHybrid", "VipUltimate", "VipQuantum", "VipDragon", "VipTotal");
    List<String> ranksList = Arrays.asList("rankCivil", "rankPedra", "rankCarvao", "rankFerro", "rankOuro", "rankDiamante", "rankEsmeralda", "rankNetherstar");

    public Map<String, String> listRanksTags() {
        Map<String, String> rankTags = new HashMap<>();
        rankTags.put("rankPedra", "&b=&f[&7Pedra&f]&b=-");
        rankTags.put("rankCarvao", "&b=&f[&8Carvão&f]&b=-");
        rankTags.put("rankFerro", "&b=&f[&fFerro&f]&b=-");
        rankTags.put("rankOuro", "&b=&f[&eOuro&f]&b=-");
        rankTags.put("rankDiamante", "&b=&f[&bDiamante&f]&b=-");
        rankTags.put("rankEsmeralda", "&b=&f[&aEsmeralda&f]&b=-");
        rankTags.put("rankNetherstar", "&b=&f[&dNetherStar&f]&b=-");
        return rankTags;
    }

    public Map<String, Integer> listPriceitem() {
        Map<String, Integer> itemCost = new HashMap<>();
        itemCost.put("rankPedra", 1);
        itemCost.put("rankCarvao", 1);
        itemCost.put("rankFerro", 1);
        itemCost.put("rankOuro", 1);
        itemCost.put("rankDiamante", 2);
        itemCost.put("rankEsmeralda", 2);
        itemCost.put("rankNetherstar", 3);
        return itemCost;
    }

    public void setRankTag(Player player, List<String> ranksList, Map<String, String> rankTags) {
        PermissionUser user = PermissionsEx.getUser(player);
        String currentRank = getCurrentRank(player, ranksList);
        if (rankTags.containsKey(currentRank)) {
            String tag = rankTags.get(currentRank);
            user.setSuffix(tag, "");
        }
    }

    public String getCurrentRank(Player player, List<String> ranksList) {
        PermissionUser user = PermissionsEx.getUser(player);
        for (String ranks : ranksList) {
            if (user.has(ranks)) {
                return ranks;
            }
        }
        return "rankCivil";
    }

    public String getNextRank(Player player, List<String> ranksList) {
        String currentRank = getCurrentRank(player, ranksList);
        int index = ranksList.indexOf(currentRank);
        if (index == ranksList.size() - 1) {
            return "rankNetherstar";
        }
        return ranksList.get(index + 1);
    }

    public void setRankPlayer(Player player, List<String> ranksList) {
        PermissionUser user = PermissionsEx.getUser(player);
        String rank = getCurrentRank(player, ranksList);
        String nextRank = getNextRank(player, ranksList);
        String group = getGroup(player, listGroups);

        user.removePermission(rank);
        user.addPermission(nextRank);

        user.removePermission("ChestShop.profit." + group + rank);
        user.removePermission("ChestShop.profit." + group);
    }

    public String getGroup(Player player, List<String> listGroups) {
        PermissionUser user = PermissionsEx.getUser(player);
        for (String vip : listGroups) {
            if (user.inGroup(vip)) {
                return vip;
            }
        }
        return "Civil";
    }

    public void removeAllPerms(Player player, List<String> ranksList, List<String> listGroups) {
        PermissionUser user = PermissionsEx.getUser(player);
        String currentGroup = user.getGroups()[0].getName();
        for (String group : listGroups) {
            if (!group.equals(currentGroup)) {
                for (String rank : ranksList) {
                    String permissionRank = "ChestShop.profit." + group + rank;
                    String permission = "ChestShop.profit." + group;
                    user.removePermission(permissionRank);
                    user.removePermission(permission);
                }
            }
        }
    }

    public void eventSetRank(String playerName) {
        Player player = getPlayer(playerName);
        PermissionManager permissionManager = PermissionsEx.getPermissionManager();
        PermissionUser user = permissionManager.getUser(playerName);
        String newGroup = user.getGroups()[0].getName();
        if (ranksList.stream().anyMatch(user::has) && listGroups.stream().anyMatch(user::inGroup)) {
            String currentRank = getCurrentRank(player, ranksList);
            String newPermission = "ChestShop.profit." + newGroup + currentRank;
            if (!user.has(newPermission)) {
                user.addPermission(newPermission);
                removeAllPerms(player, ranksList, listGroups);
            }
        } else if (ranksList.stream().noneMatch(user::has) && listGroups.stream().anyMatch(user::inGroup)) {
            String newPermission = "ChestShop.profit." + newGroup;
            if (!user.has(newPermission)) {
                user.addPermission(newPermission);
                removeAllPerms(player, ranksList, listGroups);
            }
        }
    }

    public int getRankNumber(String currentRank, List<String> ranksList) {
        return ranksList.indexOf(currentRank) + 1;
    }
}
