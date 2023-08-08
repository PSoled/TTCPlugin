package com.totalcraft.soled.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.totalcraft.soled.Commands.Vender.formatter;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmConsole;
import static com.totalcraft.soled.auction.ConfigAuction.priceMinServer;

public class MinValues implements CommandExecutor {
    boolean start = false;
    public static List<Inventory> minValues = new ArrayList<>();
    public static ItemStack itemInfo, previousPage, nextPage;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPmConsole());
            return true;
        }
        if (!start) {
            setItems();
            createInv();
            setGuiMinValues();
        }
        Player player = (Player) sender;
        player.openInventory(minValues.get(0));
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        return true;
    }

    private void setGuiMinValues() {
        TreeMap<String, Integer> sortedPriceMinServer = new TreeMap<>((s1, s2) -> {
            String[] parts1 = s1.split(":");
            String[] parts2 = s2.split(":");
            int id1 = Integer.parseInt(parts1[0]);
            int id2 = Integer.parseInt(parts2[0]);
            if (id1 != id2) {
                return Integer.compare(id1, id2);
            } else {
                short meta1 = parts1[1].equals("*") ? 0 : Short.parseShort(parts1[1]);
                short meta2 = parts2[1].equals("*") ? 0 : Short.parseShort(parts2[1]);
                return Short.compare(meta1, meta2);
            }
        });
        sortedPriceMinServer.putAll(priceMinServer);
        for (Map.Entry<String, Integer> entry : sortedPriceMinServer.entrySet()) {
            int id = Integer.parseInt(entry.getKey().split(":")[0]);
            short meta;
            if (entry.getKey().split(":")[1].equals("*")) {
                meta = 0;
            } else {
                meta = Short.parseShort(entry.getKey().split(":")[1]);
            }
            int valueMin = entry.getValue();
            String valorFormat = String.format("%s", formatter.format(valueMin));
            ItemStack item = new ItemStack(id, 0, meta);
            ItemMeta itemMeta = item.getItemMeta();
            List<String> list = new ArrayList<>();
            if (item.getTypeId() == 30188) {
                list.add(getFormatColor("&eValor Mínimo: &a3.000 (Pack)"));
            } else if (item.getTypeId() == 4362 && item.getDurability() == 6) {
                list.add(getFormatColor("&eValor Mínimo: &a20.000 (Pack)"));
            } else {
                list.add(getFormatColor("&eValor Mínimo: &a" + valorFormat));
            }
            itemMeta.setLore(list);
            item.setItemMeta(itemMeta);
            addItem(item);
        }
        start = true;
    }
    private void setItems() {
        itemInfo = new ItemStack(27206);
        ItemMeta meta = itemInfo.getItemMeta();
        List<String> list = new ArrayList<>();
        meta.setDisplayName(getFormatColor("&e&lInformação:"));
        list.add(getFormatColor("&9Aqui está os valores mínimos do Server"));
        list.add(getFormatColor("&9Eles devem ser seguidos para uma boa conduta"));
        list.add(getFormatColor("&9E para que toda economia sejá bem organizada"));
        meta.setLore(list);
        itemInfo.setItemMeta(meta);

        nextPage = new ItemStack(339);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName(getFormatColor("&bPróxima página"));
        nextPage.setItemMeta(nextMeta);

        previousPage = new ItemStack(339);
        ItemMeta previousMeta = nextPage.getItemMeta();
        previousMeta.setDisplayName(getFormatColor("&bPágina anterior"));
        previousPage.setItemMeta(previousMeta);

    }
    private boolean slotEmpty(ItemStack item) {
        if (item != null) {
            if (item.getItemMeta().hasDisplayName()) {
                String name = item.getItemMeta().getDisplayName();
                if (name.equals(itemInfo.getItemMeta().getDisplayName())) {
                    return true;
                }
                if (name.equals(previousPage.getItemMeta().getDisplayName())) {
                    return true;
                }
                return name.equals(nextPage.getItemMeta().getDisplayName());
            }
        }
        return false;
    }
    private void createInv() {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Valores Mínimos 1");
        inv.setItem(49, itemInfo);
        inv.setItem(51, nextPage);
        minValues.add(inv);
    }
    private int getInv() {
        int emptySlots = 0;
        int invNumber = 0;
        for (Inventory inv : minValues) {
            for (int i = 0; i < inv.getSize(); i++) {
                if (inv.getItem(i) == null || slotEmpty(inv.getItem(i))) {
                    emptySlots++;
                }
            }
            if (emptySlots > 9) {
                return invNumber;
            }
            invNumber++;
        }
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLACK + "Valores Mínimos " + (invNumber + 1));
        inv.setItem(49, itemInfo);
        inv.setItem(51, nextPage);
        inv.setItem(47, previousPage);
        minValues.add(inv);
        return invNumber;
    }

    private void addItem(ItemStack item) {
        int value = getInv();
        minValues.get(value).addItem(item);
    }
}
