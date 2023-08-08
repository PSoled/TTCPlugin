package com.totalcraft.soled.auction;

import com.totalcraft.soled.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.totalcraft.soled.auction.AuctionBase.*;
import static com.totalcraft.soled.auction.Hub.setItemsHub;
import static com.totalcraft.soled.auction.PlayerAuction.setItemsPlayerAuc;
import static com.totalcraft.soled.auction.SetItemAuction.setItemsSetItem;

public class ConfigAuction {
    private final Main main;

    public ConfigAuction(Main main) {
        this.main = main;
    }

    public static File auctionFile, priceFile;
    public static YamlConfiguration auctionConfig, priceConfig;
    public static Map<String, Integer> priceMinServer = new HashMap<>();
    public static List<String> invNames = new ArrayList<>();

    public void loadAuction() {
        loadData();
        loadInvNames();
        setItemsHub();
        setItemsSetItem();
        setItemsAuction();
        setItemsPlayerAuc();
        savePrice();
        loadAuctionItems();
        AuctionLog log = new AuctionLog(main);
        log.loadAuctionLog();
    }

    public void reloadAuctions() {
        saveAuction();
        AuctionItems = new ArrayList<>();
        loadItems();
        loadAuctionItems();
    }

    private void loadData() {
        Inventory inv = Bukkit.createInventory(null, 45, ChatColor.AQUA + "Leilão 1");
        AuctionItems.add(inv);
        auctionFile = new File(main.getDataFolder(), "data/leilao.yml");
        auctionConfig = YamlConfiguration.loadConfiguration(auctionFile);
        priceFile = new File(main.getDataFolder(), "pricemin.yml");
        priceConfig = YamlConfiguration.loadConfiguration(priceFile);
        priceConfig.options().header(" \nPreço minimo de itens do server\n ");
        loadItems();
    }
    private void loadItems() {
        AuctionBase auctionBase = new AuctionBase();
        for (String itemId : priceConfig.getKeys(false)) {
            int priceMin = priceConfig.getInt(itemId);
            priceMinServer.put(itemId, priceMin);
        }
        for (String nameData : auctionConfig.getKeys(false)) {
            String info = auctionConfig.getString(nameData);
            String nameCfg = nameData.split(" ")[0];
            String idCfg = info.split(" ")[0];
            String amountCfg = info.split(" ")[1];
            String priceCfg = info.split(" ")[2];
            auctionBase.addItemAuctionCfg(nameCfg, idCfg, amountCfg, priceCfg);
        }
    }
    private void savePrice() {
        for (Map.Entry<String, Integer> entry : priceMinServer.entrySet()) {
            priceConfig.set(entry.getKey(), entry.getValue());
        }
        try {
            priceConfig.save(priceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAuction() {
        clearCacheData();
        for (Inventory auction : AuctionItems) {
            for (ItemStack auctionItem : auction) {
                if (auctionItem != null) {
                    if (auctionItem.getItemMeta().hasLore()) {
                        int data = 1;
                        String name = auctionItem.getItemMeta().getLore().get(0).split(" ")[1] + " " + data;
                        while (auctionConfig.contains(name)) {
                            data++;
                            name = auctionItem.getItemMeta().getLore().get(0).split(" ")[1] + " " + data;
                        }
                        String price = auctionItem.getItemMeta().getLore().get(1).split(" ")[2];
                        String id = auctionItem.getTypeId() + ":" + auctionItem.getDurability();
                        int amount = auctionItem.getAmount();
                        auctionConfig.set(name, id + " " + amount + " " + price);
                    }
                }
            }
        }
        try {
            auctionConfig.save(auctionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearCacheData() {
        List<String> auctionKeys = new ArrayList<>(auctionConfig.getKeys(false));
        for (String nameData : auctionKeys) {
            auctionConfig.set(nameData, null);
        }
        try {
            auctionConfig.save(auctionFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadInvNames() {
        String invSell = ChatColor.AQUA + "Venda: " + ChatColor.BLACK;
        String invAuction = ChatColor.AQUA + "Leilão";
        String invAucPlayer = ChatColor.BLACK + "Administrar o seu leilão";
        String invBuyItem = ChatColor.BLACK + "Item: " + ChatColor.GOLD;
        String invCenter = ChatColor.BLACK + "Centro do Leilão";
        invNames.add(invSell);
        invNames.add(invAuction);
        invNames.add(invAucPlayer);
        invNames.add(invBuyItem);
        invNames.add(invCenter);
    }
}
