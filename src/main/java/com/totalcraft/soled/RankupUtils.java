package com.totalcraft.soled;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankupUtils {

    List<String> listGroups = Arrays.asList("Civil", "VipAdvanced", "VipHybrid", "VipUltimate", "VipQuantum", "VipDragon", "VipTotal");
    List<String> ranksList = Arrays.asList("rank.civil", "rank.pedra", "rank.carvao", "rank.ferro", "rank.ouro", "rank.diamante", "rank.esmeralda", "rank.netherstar");

    public Map<String, String> listRanksTags() {
        Map<String, String> rankTags = new HashMap<>();
        rankTags.put("rank.pedra", "&7[Pedra]");
        rankTags.put("rank.carvao", "&8[Carvão]");
        rankTags.put("rank.ferro", "&f[Ferro]");
        rankTags.put("rank.ouro", "&6[Ouro]");
        rankTags.put("rank.diamante", "&b[Diamante]");
        rankTags.put("rank.esmeralda", "&a[Esmeralda]");
        rankTags.put("rank.netherstar", "&d[NetherStar]");
        return rankTags;
    }

    public Map<String, Integer> listPriceitem() {
        Map<String, Integer> itemCost = new HashMap<>();
        itemCost.put("rank.pedra", 1);
        itemCost.put("rank.carvao", 1);
        itemCost.put("rank.ferro", 1);
        itemCost.put("rank.ouro", 1);
        itemCost.put("rank.diamante", 2);
        itemCost.put("rank.esmeralda", 2);
        itemCost.put("rank.netherstar", 3);
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
        return "rank.civil";
    }

    public String getNextRank(Player player, List<String> ranksList) {
        String currentRank = getCurrentRank(player, ranksList);
        int index = ranksList.indexOf(currentRank);
        if (index == ranksList.size() - 1) {
            return "rank.netherstar";
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

        user.removePermission("ChestShop.profit." + group + "." + rank);
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
                    String permission = "ChestShop.profit." + group + "." + rank;
                    if (user.has(permission)) {
                        user.removePermission(permission);
                    }
                }
            }
        }
    }

    public int getRankNumber(String currentRank, List<String> ranksList) {
        return ranksList.indexOf(currentRank) + 1;
    }

}
