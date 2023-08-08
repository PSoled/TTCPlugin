package com.totalcraft.soled.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChestShopUtils {
    public static List<String> ItemsCancelled = new ArrayList<>();
    public static boolean isItemCancelShop(String idItem, String metaItem) {
        for (String s : ItemsCancelled) {
            String[] split = s.split(":");
            if (split.length > 1) {
                if (split[1].equals("*")) {
                    if (idItem.equals(split[0])) {
                        return true;
                    }
                 } else if (idItem.equals(split[0]) && metaItem.equals(split[1])) {
                    return true;
                }
            } else if (idItem.equals(s) && metaItem.equals("0")) {
                return true;
            }
        }
        return false;
    }
}
