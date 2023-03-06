package com.totalcraft.soled.Utils;

import com.totalcraft.soled.Commands.Vender;
import com.totalcraft.soled.Configs.PriceItems;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class VenderUtils {
    public static Map<String, Double> priceItems = new HashMap<>();
    static Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

    public void addItem(int id, int meta, double valor) {
        priceItems.put(id + ":" + meta, valor);
        PriceItems.saveConfigPrice();

    }

    public static Map<String, Double> priceItemsNether() {
        Map<String, Double> priceItemsNether = new HashMap<>();
        priceItemsNether.put("11723:11", 1500000.0);
        priceItemsNether.put("27186:0", 1000.0);
        priceItemsNether.put("27215:0", 350.0);
        priceItemsNether.put("18006:7", 3500.0);
        return priceItemsNether;
    }

    public static double getAmount(Player player, Map<String, Double> items) {
        PermissionUser user = PermissionsEx.getUser(player);
        Map<Integer, ItemStack> removeItems = new HashMap<>();
        double totalNether = 0;
        double totalValue = 0.0;
        if (user.has("rankNetherstar")) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack != null) {
                    int id = stack.getTypeId();
                    int meta = stack.getDurability();
                    String key = id + ":" + meta;
                    double value = priceItemsNether().getOrDefault(key, 0.0);
                    if (value > 0) {
                        int count = stack.getAmount();
                        double itemValue = count * value;
                        totalNether += itemValue;
                        if (count <= stack.getMaxStackSize()) {
                            removeItems.put(i, stack.clone());
                        } else {
                            ItemStack splitStack = stack.clone();
                            splitStack.setAmount(stack.getMaxStackSize());
                            removeItems.put(i, splitStack);
                            stack.setAmount(count - stack.getMaxStackSize());
                        }
                    }
                }
            }
            removeItems.forEach((slot, item) -> player.getInventory().setItem(slot, null));
            totalValue += totalNether;
        }
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack != null) {
                int id = stack.getTypeId();
                int meta = stack.getDurability();
                String key = id + ":" + meta;
                double value = items.getOrDefault(key, 0.0);
                if (value > 0) {
                    int count = stack.getAmount();
                    double itemValue = count * value;
                    totalValue += itemValue;
                    if (count <= stack.getMaxStackSize()) {
                        removeItems.put(i, stack.clone());
                    } else {
                        ItemStack splitStack = stack.clone();
                        splitStack.setAmount(stack.getMaxStackSize());
                        removeItems.put(i, splitStack);
                        stack.setAmount(count - stack.getMaxStackSize());
                    }
                }
            }
        }
        removeItems.forEach((slot, item) -> player.getInventory().setItem(slot, null));
        return totalValue;
    }

    public static Map<String, Integer> listProfit() {
        Map<String, Integer> listProfit = new HashMap<>();
        String profit = "ChestShop.profit.";
        listProfit.put(profit + "Civilrankpedra", 3);
        listProfit.put(profit + "Civilrankcarvao", 6);
        listProfit.put(profit + "Civilrankferro", 9);
        listProfit.put(profit + "Civilrankouro", 12);
        listProfit.put(profit + "Civilrankdiamante", 15);
        listProfit.put(profit + "Civilrankesmeralda", 18);
        listProfit.put(profit + "Civilranknetherstar", 21);
        listProfit.put(profit + "VipAdvanced", 3);
        listProfit.put(profit + "VipAdvancedrankpedra", 6);
        listProfit.put(profit + "VipAdvancedrankcarvao", 9);
        listProfit.put(profit + "VipAdvancedrankferro", 12);
        listProfit.put(profit + "VipAdvancedrankouro", 15);
        listProfit.put(profit + "VipAdvancedrankdiamante", 18);
        listProfit.put(profit + "VipAdvancedrankesmeralda", 21);
        listProfit.put(profit + "VipAdvancedranknetherstar", 24);
        listProfit.put(profit + "VipHybrid", 6);
        listProfit.put(profit + "VipHybridrankpedra", 9);
        listProfit.put(profit + "VipHybridrankcarvao", 12);
        listProfit.put(profit + "VipHybridrankferro", 15);
        listProfit.put(profit + "VipHybridrankouro", 18);
        listProfit.put(profit + "VipHybridrankdiamante", 21);
        listProfit.put(profit + "VipHybridrankesmeralda", 24);
        listProfit.put(profit + "VipHybridranknetherstar", 27);
        listProfit.put(profit + "VipUltimate", 9);
        listProfit.put(profit + "VipUltimaterankpedra", 12);
        listProfit.put(profit + "VipUltimaterankcarvao", 15);
        listProfit.put(profit + "VipUltimaterankferro", 18);
        listProfit.put(profit + "VipUltimaterankouro", 21);
        listProfit.put(profit + "VipUltimaterankdiamante", 24);
        listProfit.put(profit + "VipUltimaterankesmeralda", 27);
        listProfit.put(profit + "VipUltimateranknetherstar", 30);
        listProfit.put(profit + "VipQuantum", 12);
        listProfit.put(profit + "VipQuantumrankpedra", 15);
        listProfit.put(profit + "VipQuantumrankcarvao", 18);
        listProfit.put(profit + "VipQuantumrankferro", 21);
        listProfit.put(profit + "VipQuantumrankouro", 24);
        listProfit.put(profit + "VipQuantumrankdiamante", 27);
        listProfit.put(profit + "VipQuantumrankesmeralda", 30);
        listProfit.put(profit + "VipQuantumranknetherstar", 33);
        listProfit.put(profit + "VipDragon", 15);
        listProfit.put(profit + "VipDragonrankpedra", 18);
        listProfit.put(profit + "VipDragonrankcarvao", 21);
        listProfit.put(profit + "VipDragonrankferro", 24);
        listProfit.put(profit + "VipDragonrankouro", 27);
        listProfit.put(profit + "VipDragonrankdiamante", 30);
        listProfit.put(profit + "VipDragonrankesmeralda", 33);
        listProfit.put(profit + "VipDragonranknetherstar", 36);
        listProfit.put(profit + "VipTotal", 18);
        listProfit.put(profit + "VipTotalrankpedra", 21);
        listProfit.put(profit + "VipTotalrankcarvao", 24);
        listProfit.put(profit + "VipTotalrankferro", 27);
        listProfit.put(profit + "VipTotalrankouro", 30);
        listProfit.put(profit + "VipTotalrankdiamante", 33);
        listProfit.put(profit + "VipTotalrankesmeralda", 36);
        listProfit.put(profit + "VipTotalranknetherstar", 39);

        return listProfit;
    }

    public static int getProfit(Player player) {
        PermissionUser user = PermissionsEx.getUser(player);
        for (String key : listProfit().keySet()) {
            if (user.has(key)) {
                return listProfit().get(key);
            }
        }
        return 0;
    }

    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    static ScheduledFuture<?> scheduledFuture, cancelTask;
    public static void venderAuto(Player player, Map<String, Double> list) {
        player.sendMessage(getPmTTC("&eVender auto " + (list.equals(VenderUtils.priceItems) ?  "normal" : "&dNetherStar") + " &aAtivado"));
        if (cancelTask == null) {
            cancelTaskVender();
        }
        if (scheduledFuture == null) {
            scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
                Iterator<Map.Entry<String, Integer>> it = Vender.playerVender.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Integer> entry = it.next();
                    Player playerObjeto = Bukkit.getPlayer(entry.getKey());
                    if (entry.getValue() >= 15) {
                        it.remove();
                        playerObjeto.sendMessage(getPmTTC("&cVender Auto desativado após 15 Segundos Inativo"));
                    }

                    double valor;
                    valor = getAmount(playerObjeto, list);

                    if (valor == 0) {
                        entry.setValue(entry.getValue() + 1);
                    }

                    double profit = getProfit(playerObjeto);
                    double total = (valor * profit / 100) + valor;
                    if (total > 0) {
                        entry.setValue(0);
                        economy.depositPlayer(playerObjeto.getName(), total);
                        String valorTotal = String.format("%s", Vender.formatter.format(total));
                        playerObjeto.sendMessage(getPmTTC("&aVocê vendeu os itens no seu inventário por &e" + valorTotal + " &aReais!"));
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    public static void cancelTaskVender() {
        cancelTask = scheduler.scheduleAtFixedRate(() -> {
            if (Vender.playerVender == null) {
                scheduledFuture.cancel(true);
                cancelTask.cancel(true);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
