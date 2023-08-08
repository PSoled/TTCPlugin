package com.totalcraft.soled.Crates;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.totalcraft.soled.Crates.CrateBase.getNameRarity;
import static com.totalcraft.soled.Crates.CrateBase.keyInventory;
import static com.totalcraft.soled.Crates.CrateEvent.playerInKeyEvent;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static org.bukkit.Sound.EXPLODE;
import static org.bukkit.Sound.LEVEL_UP;

public class CrateKey {
    private final Plugin plugin;

    public CrateKey(Plugin plugin) {
        this.plugin = plugin;
    }

    public static List<String> playerKeyEvent = new ArrayList<>();
    public static List<ItemStack> ItemKeyEvent = new ArrayList<>();
    public static List<ItemStack> arrows = new ArrayList<>();

    public Inventory getInvRandomItem(String keyName, Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, getPmTTC("&bCrate"));
        String invName = getFormatColor("&7Key:&c " + keyName + "-config");
        Inventory invKey = keyInventory.get(invName);
        List<String> list = new ArrayList<>();
        for (ItemStack item : invKey.getContents()) {
            if (item != null) {
                if (item.getItemMeta().hasLore()) {
                    String chance = item.getItemMeta().getLore().get(1).split(" ")[1];
                    list.add(item.getTypeId() + " " + item.getAmount() + " " + item.getDurability() + " " + chance);
                }
            }
        }
        randomItem(list, inv, player, keyName);
        return inv;
    }

    public String[] getRandomItem(String keyName) {
        String invName = getFormatColor("&7Key:&c " + keyName + "-config");
        Inventory invKey = keyInventory.get(invName);
        List<String> list = new ArrayList<>();
        for (ItemStack item : invKey.getContents()) {
            if (item != null) {
                if (item.getItemMeta().hasLore()) {
                    String chance = item.getItemMeta().getLore().get(1).split(" ")[1];
                    list.add(item.getTypeId() + " " + item.getAmount() + " " + item.getDurability() + " " + chance);
                }
            }
        }
        List<String> weightedItems = new ArrayList<>();
        for (String itemChance : list) {
            String[] parts = itemChance.split(" ");
            int typeId = Integer.parseInt(parts[0]);
            int amount = Integer.parseInt(parts[1]);
            short durability = Short.parseShort(parts[2]);
            double chance = Double.parseDouble(parts[3]);
            int weight = (int) (chance * 100);
            for (int i = 0; i < weight; i++) {
                weightedItems.add(typeId + " " + amount + " " + durability + " " + chance);
            }
        }
        String[] chosen = new String[0];
        if (weightedItems.size() > 0) {
            chosen = weightedItems.get(new Random().nextInt(weightedItems.size())).split(" ");
        }
        return chosen;
    }


    private void giveawayItem(Player player, ItemStack item, String chance) {
        double chanceItem = Double.parseDouble(chance);
        String rarity = getNameRarity(chanceItem);
        int value = playerInKeyEvent.get(player.getName());
        playerInKeyEvent.put(player.getName(), value - 1);
        player.sendMessage(getPmTTC("&aParabéns você ganhou &b" + item.getType().name() + " &aDa raridade " + rarity));
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), item);
        } else {
            player.getInventory().addItem(item);
        }
        if (rarity != null) {
            if (rarity.contains("Lendário") || rarity.contains("Muito Raro") || rarity.contains("Épico")) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.playSound(online.getLocation(), LEVEL_UP, 1, 1);
                }
                Bukkit.broadcastMessage(getPmTTC("&b" + player.getName() + " &fAcaba de abrir um &bCrate &fe ganhar um item " + rarity));
            }
        }
    }

    public static ScheduledExecutorService schedulerCK = new ScheduledThreadPoolExecutor(1);
    static List<Integer> slotItems = Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17);
    static List<Integer> slot1 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
    static List<Integer> slot2 = Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26);

    private void randomItem(List<String> list, Inventory inv, Player player, String keyName) {
        playerKeyEvent.add(player.getName());
        final int[] time = {20};
        List<ItemStack> items = new ArrayList<>();
        for (String name : list) {
            String[] part = name.split(" ");
            ItemStack item = new ItemStack(Integer.parseInt(part[0]), Integer.parseInt(part[1]), Short.parseShort(part[2]));
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(getFormatColor(getNameRarity(Double.parseDouble(part[3]))));
            meta.setLore(lore);
            item.setItemMeta(meta);
            items.add(item);
        }
        String[] chosen = getRandomItem(keyName);
        int typeId = Integer.parseInt(chosen[0]);
        int amount = Integer.parseInt(chosen[1]);
        short durability = Short.parseShort(chosen[2]);
        String chance = chosen[3];
        ItemStack itemWin = new ItemStack(typeId, amount, durability);
        final int[] index = {0};
        final int[] index1 = {0};
        final int[] index2 = {0};
        final int[] index3 = {0};
        final int[] startSlot = {0};
        final int[] startSlot1 = {0};
        final ScheduledFuture<?>[] task1 = new ScheduledFuture<?>[1];
        task1[0] = schedulerCK.scheduleAtFixedRate(() -> {
            for (int number : slotItems) {
                if (index[0] > items.size() - 1) index[0] = 0;
                inv.setItem(number, items.get(index[0]));
                index[0]++;
            }
            for (int number : slot1) {
                if (index1[0] > ItemKeyEvent.size() - 1) index1[0] = 0;
                inv.setItem(number, ItemKeyEvent.get(index1[0]));
                index1[0]++;
            }
            for (int number : slot2) {
                if (index2[0] > ItemKeyEvent.size() - 1) index2[0] = 0;
                inv.setItem(number, ItemKeyEvent.get(index2[0]));
                index2[0]++;
            }
            if (index3[0] > arrows.size() - 1) index3[0] = 0;
            inv.setItem(22, arrows.get(index3[0]));
            index3[0]++;
            index[0] = startSlot[0];
            index1[0] = startSlot1[0];
            index2[0] = startSlot1[0];
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
            if (time[0] <= 0) {
                task1[0].cancel(true);
                time[0] = 50;
                index3[0] = index1[0] + 1;
                final ScheduledFuture<?>[] task2 = new ScheduledFuture<?>[1];
                task2[0] = schedulerCK.scheduleAtFixedRate(() -> {
                    for (int number : slotItems) {
                        if (index[0] > items.size() - 1) {
                            index[0] = 0;
                        }
                        inv.setItem(number, items.get(index[0]));
                        index[0]++;
                    }
                    for (int number : slot1) {
                        if (index1[0] > ItemKeyEvent.size() - 1) index1[0] = 0;
                        inv.setItem(number, ItemKeyEvent.get(index1[0]));
                        index1[0]++;
                    }
                    for (int number : slot2) {
                        if (index2[0] > ItemKeyEvent.size() - 1) index2[0] = 0;
                        inv.setItem(number, ItemKeyEvent.get(index2[0]));
                        index2[0]++;
                    }
                    if (index3[0] > arrows.size() - 1) index3[0] = 0;
                    inv.setItem(22, arrows.get(index3[0]));
                    index3[0]++;
                    index[0] = startSlot[0];
                    index1[0] = startSlot1[0];
                    index2[0] = startSlot1[0];
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    if (time[0] <= 0) {
                        task2[0].cancel(true);
                        time[0] = 50;
                        index3[0] = index1[0] + 1;
                        final ScheduledFuture<?>[] task3 = new ScheduledFuture<?>[1];
                        task3[0] = schedulerCK.scheduleAtFixedRate(() -> {
                            for (int number : slotItems) {
                                if (index[0] > items.size() - 1) index[0] = 0;
                                inv.setItem(number, items.get(index[0]));
                                index[0]++;
                            }
                            for (int number : slot1) {
                                if (index1[0] > ItemKeyEvent.size() - 1) index1[0] = 0;
                                inv.setItem(number, ItemKeyEvent.get(index1[0]));
                                index1[0]++;
                            }
                            for (int number : slot2) {
                                if (index2[0] > ItemKeyEvent.size() - 1) index2[0] = 0;
                                inv.setItem(number, ItemKeyEvent.get(index2[0]));
                                index2[0]++;
                            }
                            if (index3[0] > arrows.size() - 1) index3[0] = 0;
                            inv.setItem(22, arrows.get(index3[0]));
                            index3[0]++;
                            index[0] = startSlot[0];
                            index1[0] = startSlot1[0];
                            index2[0] = startSlot1[0];
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                            if (time[0] <= 0) {
                                task3[0].cancel(true);
                                time[0] = 50;
                                index3[0] = index1[0] + 1;
                                final ScheduledFuture<?>[] task4 = new ScheduledFuture<?>[1];
                                task4[0] = schedulerCK.scheduleAtFixedRate(() -> {
                                    for (int number : slotItems) {
                                        if (index[0] > items.size() - 1) index[0] = 0;
                                        inv.setItem(number, items.get(index[0]));
                                        index[0]++;
                                    }
                                    for (int number : slot1) {
                                        if (index1[0] > ItemKeyEvent.size() - 1) index1[0] = 0;
                                        inv.setItem(number, ItemKeyEvent.get(index1[0]));
                                        index1[0]++;
                                    }
                                    for (int number : slot2) {
                                        if (index2[0] > ItemKeyEvent.size() - 1) index2[0] = 0;
                                        inv.setItem(number, ItemKeyEvent.get(index2[0]));
                                        index2[0]++;
                                    }
                                    if (index3[0] > arrows.size() - 1) index3[0] = 0;
                                    inv.setItem(22, arrows.get(index3[0]));
                                    index3[0]++;
                                    index[0] = startSlot[0];
                                    index1[0] = startSlot1[0];
                                    index2[0] = startSlot1[0];
                                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                                    if (time[0] <= 0) {
                                        task4[0].cancel(true);
                                        index3[0] = index1[0] + 1;
                                        final ScheduledFuture<?>[] task5 = new ScheduledFuture<?>[1];
                                        task5[0] = schedulerCK.scheduleAtFixedRate(() -> {
                                            for (int number : slotItems) {
                                                if (index[0] > items.size() - 1) index[0] = 0;
                                                inv.setItem(number, items.get(index[0]));
                                                index[0]++;
                                            }
                                            for (int number : slot1) {
                                                if (index1[0] > ItemKeyEvent.size() - 1) index1[0] = 0;
                                                inv.setItem(number, ItemKeyEvent.get(index1[0]));
                                                index1[0]++;
                                            }
                                            for (int number : slot2) {
                                                if (index2[0] > ItemKeyEvent.size() - 1) index2[0] = 0;
                                                inv.setItem(number, ItemKeyEvent.get(index2[0]));
                                                index2[0]++;
                                            }
                                            if (index3[0] > arrows.size() - 1) index3[0] = 0;
                                            inv.setItem(22, arrows.get(index3[0]));
                                            index3[0]++;
                                            index[0] = startSlot[0];
                                            index1[0] = startSlot1[0];
                                            index2[0] = startSlot1[0];
                                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                                            ItemStack itemMiddle = inv.getItem(13);
                                            if (itemMiddle.getTypeId() == itemWin.getTypeId() && itemMiddle.getDurability() == itemWin.getDurability() && itemMiddle.getAmount() == itemWin.getAmount()) {
                                                task5[0].cancel(true);
                                                checkCancel(player, itemWin, chance);
                                            }
                                            startSlot[0]++;
                                            startSlot1[0]++;
                                            if (startSlot[0] > items.size() - 1) startSlot[0] = 0;
                                            if (startSlot1[0] > ItemKeyEvent.size() - 1) startSlot1[0] = 0;
                                        }, 0, 250, TimeUnit.MILLISECONDS);
                                    }
                                    startSlot[0]++;
                                    startSlot1[0]++;
                                    if (startSlot[0] > items.size() - 1) startSlot[0] = 0;
                                    if (startSlot1[0] > ItemKeyEvent.size() - 1) startSlot1[0] = 0;
                                    time[0] -= 4;
                                }, 0, 200, TimeUnit.MILLISECONDS);
                            }
                            startSlot[0]++;
                            startSlot1[0]++;
                            if (startSlot[0] > items.size() - 1) startSlot[0] = 0;
                            if (startSlot1[0] > ItemKeyEvent.size() - 1) startSlot1[0] = 0;
                            time[0] -= 3;
                        }, 0, 150, TimeUnit.MILLISECONDS);
                    }
                    startSlot[0]++;
                    startSlot1[0]++;
                    if (startSlot[0] > items.size() - 1) startSlot[0] = 0;
                    if (startSlot1[0] > ItemKeyEvent.size() - 1) startSlot1[0] = 0;
                    time[0] -= 2;
                }, 0, 100, TimeUnit.MILLISECONDS);
            }
            startSlot[0]++;
            startSlot1[0]++;
            if (startSlot[0] > items.size() - 1) startSlot[0] = 0;
            if (startSlot1[0] > ItemKeyEvent.size() - 1) startSlot1[0] = 0;
            time[0]--;
        }, 100, 50, TimeUnit.MILLISECONDS);
    }

    private void checkCancel(Player player, ItemStack itemWin, String chance) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            throwFirework(player);
            throwFirework(player);
            throwFirework(player);
            giveawayItem(player, itemWin, chance);
            player.playSound(player.getLocation(), EXPLODE, 1, 1);
            player.closeInventory();
        }, 20);
    }

    private void throwFirework(Player player) {
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.BLACK)
                .withFade(Color.AQUA)
                .with(FireworkEffect.Type.STAR)
                .trail(true)
                .build();
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(effect);
        meta.setPower(0);
        firework.setFireworkMeta(meta);
    }
}

