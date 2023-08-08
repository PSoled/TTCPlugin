package com.totalcraft.soled.Crates;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.totalcraft.soled.Commands.Keys.giveKeyCrate;
import static com.totalcraft.soled.Crates.CrateBase.*;
import static com.totalcraft.soled.Crates.CrateConfig.saveItemsCrate;
import static com.totalcraft.soled.Crates.KeysShop.invKeysShop;
import static com.totalcraft.soled.Crates.KeysShop.setKeysInShop;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;

public class CrateEvent implements Listener {

    List<String> playerCreateKey = new ArrayList<>();
    List<String> playerChangeItem = new ArrayList<>();
    List<String> playerSetLore = new ArrayList<>();
    Map<String, ItemStack> playerSaveKey = new HashMap<>();
    Map<String, ItemStack> playerSaveItem = new HashMap<>();
    public static Map<String, Integer> playerInKeyEvent = new HashMap<>();

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack item = event.getCurrentItem();
        String itemName = null;
        Player player = (Player) event.getWhoClicked();

        if (inventory == null || item == null || item.getType() == Material.AIR) {
            return;
        }

        if (inventory.getName().equals(getPmTTC("&bCrate"))) {
            event.setCancelled(true);
        }

        if (item.getItemMeta().hasDisplayName()) {
            itemName = item.getItemMeta().getDisplayName();
        }
        if (inventory.getName().equals(invHubCrate.getName())) {
            event.setCancelled(true);
            if (itemName != null) {
                if (itemName.equals(createKey.getItemMeta().getDisplayName())) {
                    player.closeInventory();
                    player.sendMessage(getPmTTC("&bDigite o Nome da Key"));
                    playerCreateKey.add(player.getName());
                }
            }
            if (item.getTypeId() == 11822) {
                player.openInventory(createInvConfigKey(item));
                playerSaveKey.put(player.getName(), item);
            }
        }

        if (inventory.getName().equals(ChatColor.BLACK + "Config Crate")) {
            event.setCancelled(true);
            if (item.getTypeId() == 11822) {
                player.sendMessage(getPmTTC("&aVocê pegou a key"));
                player.closeInventory();
                ItemStack key = playerSaveKey.get(player.getName());
                ItemStack newKey = key.clone();
                ItemMeta meta = newKey.getItemMeta();
                meta.setDisplayName(getPmTTC("&bKey Crate"));
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(key.getItemMeta().getLore().get(1));
                meta.setLore(lore);
                newKey.setItemMeta(meta);
                player.getInventory().addItem(newKey);
            }
            if (itemName != null) {
                if (itemName.equals(deleteKey.getItemMeta().getDisplayName())) {
                    ItemStack i = playerSaveKey.get(player.getName());
                    removeKeyCrate(i.getItemMeta().getLore().get(1));
                    player.closeInventory();
                    player.sendMessage(getPmTTC("&aKey deletada com sucesso!"));
                    setKeysInShop();
                }
                if (itemName.equals(addItems.getItemMeta().getDisplayName())) {
                    String key = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1);
                    Inventory inv = keyInventory.get(key + "-add");
                    player.openInventory(inv);
                }
                if (itemName.equals(configItems.getItemMeta().getDisplayName())) {
                    String key = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1);
                    Inventory inv = keyInventory.get(key + "-config");
                    player.openInventory(inv);
                }
                if (itemName.equals(setLore.getItemMeta().getDisplayName())) {
                    player.sendMessage(getPmTTC("&bDigite o nome da lore"));
                    player.closeInventory();
                    playerSetLore.add(player.getName());
                }
            }
        }
        if (inventory.getName().equals(ChatColor.BLACK + "Add Items Key")) {
            if (itemName != null) {
                if (itemName.equals(saveInv.getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                    List<String> list = new ArrayList<>();
                    list.add("");
                    list.add(getFormatColor("&7Chance:&e 0.00 %"));
                    for (int i = 0; i < 54; i++) {
                        ItemStack saveItem = inventory.getItem(i);
                        if (saveItem != null) {
                            if (!saveItem.getItemMeta().hasLore()) {
                                ItemMeta meta = saveItem.getItemMeta();
                                meta.setLore(list);
                                saveItem.setItemMeta(meta);
                                inventory.setItem(i, saveItem);
                            }
                        }
                    }
                }
                String nameInv = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1) + "-config";
                Inventory getInv = keyInventory.get(nameInv);
                getInv.clear();
                for (int i = 0; i < 54; i++) {
                    ItemStack saveItem = inventory.getItem(i);
                    if (saveItem != null && !itemName.equals(saveItem.getItemMeta().getDisplayName())) {
                        getInv.setItem(i, saveItem);
                    }
                }
                getInv.setItem(53, returnHub);
                player.openInventory(invHubCrate);
                saveItemsCrate();
                player.sendMessage(getPmTTC("&aSalvado com Sucesso!"));
            }
        }

        if (inventory.getName().equals(ChatColor.BLACK + "Config Items Key")) {
            event.setCancelled(true);
            if (itemName != null) {
                if (itemName.equals(returnHub.getItemMeta().getDisplayName())) {
                    player.openInventory(invHubCrate);
                    return;
                }
            }
            if (inventory.contains(item)) {
                playerSaveItem.put(player.getName(), item);
                player.openInventory(createInvConfigItem(item));
            }
        }

        if (inventory.getName().equals(ChatColor.BLACK + "Config Item")) {
            event.setCancelled(true);
            if (itemName != null) {
                if (itemName.equals(deleteItem.getItemMeta().getDisplayName())) {
                    String keyConfig = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1) + "-config";
                    String keyAdd = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1) + "-add";
                    ItemStack itemKey = playerSaveItem.get(player.getName());
                    removeItemKey(itemKey, keyConfig);
                    removeItemKey(itemKey, keyAdd);
                    Inventory inv = keyInventory.get(keyConfig);
                    player.openInventory(inv);
                    player.sendMessage(getPmTTC("&aItem removido com Sucesso!"));
                    saveItemsCrate();
                }
                if (itemName.equals(changeProb.getItemMeta().getDisplayName())) {
                    player.sendMessage(getPmTTC("&bDigite o valor da chance"));
                    player.closeInventory();
                    playerChangeItem.add(player.getName());
                }
            }
        }

        if (inventory.getName().equals(getPmTTC("&9Keys Shop"))) {
            event.setCancelled(true);
            if (item.getTypeId() == 11822) {
                String keyName = item.getItemMeta().getDisplayName();
                String config = getFormatColor("&7Key:&c " + keyName.substring(2) + "-config");
                Inventory invConfig = keyInventory.get(config);
                Inventory invKey = Bukkit.createInventory(null, 54, getFormatColor("&0Key: " + keyName));
                invKey.setItem(53, returnKeys);
                if (item.getItemMeta().hasLore()) {
                    String[] parts = item.getItemMeta().getLore().get(1).split(" ");
                    if (parts.length > 2) {
                        long value = Long.parseLong(parts[1]);
                        if (parts[2].contains("Cash")) {
                            ItemMeta buyKeyMeta = buyKey.getItemMeta();
                            buyKeyMeta.setDisplayName(getFormatColor("&eComprar Key Por:&b " + value + " Cash"));
                            buyKey.setItemMeta(buyKeyMeta);
                            invKey.setItem(49, buyKey);
                        }
                        if (parts[2].contains("Money")) {
                            ItemMeta buyKeyMeta = buyKey.getItemMeta();
                            buyKeyMeta.setDisplayName(getFormatColor("&eComprar Key Por:&b " + value + " Money"));
                            buyKey.setItemMeta(buyKeyMeta);
                            invKey.setItem(49, buyKey);
                        }
                    }
                }
                for (ItemStack itemConfig : invConfig.getContents()) {
                    if (itemConfig != null) {
                        if (itemConfig.getItemMeta().hasLore()) {
                            double chance = Double.parseDouble(itemConfig.getItemMeta().getLore().get(1).split(" ")[1]);
                            String rarity = getNameRarity(chance);
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add(getFormatColor(rarity));
                            ItemStack itemKey = new ItemStack(itemConfig.getTypeId(), itemConfig.getAmount(), itemConfig.getDurability());
                            ItemMeta meta = itemKey.getItemMeta();
                            meta.setLore(lore);
                            itemKey.setItemMeta(meta);
                            invKey.addItem(itemKey);
                        }
                    }
                }
                player.openInventory(invKey);
            }
        }
        if (inventory.getName().contains(getFormatColor("&0Key: "))) {
            event.setCancelled(true);
            if (itemName != null) {
                if (itemName.equals(returnKeys.getItemMeta().getDisplayName())) {
                    player.openInventory(invKeysShop);
                    return;
                }
                long value = Long.parseLong(itemName.split(" ")[3]);
                if (itemName.contains("Cash")) {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(getPmTTC("&cVocê precisa liberar 1 slot para comprar essa key"));
                        return;
                    }
                    String keyName = inventory.getName().split(" ")[1].substring(2);
                    player.performCommand("comprarkey " + player.getName() + " " + keyName + " " + value);
                }
                if (itemName.contains("Money")) {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(getPmTTC("&cVocê precisa liberar 1 slot para comprar essa key"));
                        return;
                    }
                    Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
                    if (!economy.has(player.getName(), value)) {
                        player.sendMessage(getPmTTC("&cVocê não tem money para comprar este item"));
                        player.closeInventory();
                        return;
                    }
                    economy.withdrawPlayer(player.getName(), value);
                    String keyName = inventory.getName().split(" ")[1].substring(2);
                    giveKeyCrate(Bukkit.getConsoleSender(), player, 1, keyName);
                    player.sendMessage(getPmTTC("&aVocê comprou a key &b" + keyName + " &aCom Sucesso"));
                }
            }
        }
    }

    @EventHandler
    public void Chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        if (playerCreateKey.contains(player.getName())) {
            event.setCancelled(true);
            for (String keyName : keyInventory.keySet()) {
                String rs = keyName.split(" ")[1].split("-")[0];
                if (rs.equals(msg)) {
                    player.sendMessage(getPmTTC("&cJá existe um key com este nome"));
                    return;
                }
            }
            addKeyCrate(msg, null);
            player.sendMessage(getPmTTC("&aCriado nova key com Sucesso!"));
            playerCreateKey.remove(player.getName());
            setKeysInShop();
        }

        if (playerSetLore.contains(player.getName())) {
            event.setCancelled(true);
            ItemStack key = playerSaveKey.get(player.getName());
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(key.getItemMeta().getLore().get(1));
            lore.add(getFormatColor(msg));
            ItemMeta meta = key.getItemMeta();
            meta.setLore(lore);
            key.setItemMeta(meta);
            player.sendMessage(getPmTTC("&aSetado a Lore na key com Sucesso!"));
            playerSetLore.remove(player.getName());
            setKeysInShop();
        }

        if (playerChangeItem.contains(player.getName())) {
            event.setCancelled(true);
            double value;
            try {
                value = Double.parseDouble(msg);
            } catch (Exception ignored) {
                player.sendMessage(getPmTTC("&cVocê digitou um valor incorreto"));
                return;
            }
            List<String> list = new ArrayList<>();
            list.add("");
            list.add(getFormatColor("&7Chance:&e " + value + " %"));
            ItemStack newItem = playerSaveItem.get(player.getName());
            ItemMeta meta = newItem.getItemMeta();
            meta.setLore(list);
            newItem.setItemMeta(meta);
            String keyConfig = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1) + "-config";
            String keyAdd = playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1) + "-add";
            setItemKeyCrate(newItem, keyConfig);
            setItemKeyCrate(newItem, keyAdd);
            player.sendMessage(getPmTTC("&aValor da chance alterado com Sucesso!"));
            playerChangeItem.remove(player.getName());
            Inventory keyInv = keyInventory.get(playerSaveKey.get(player.getName()).getItemMeta().getLore().get(1) + "-config");
            player.openInventory(keyInv);
            saveItemsCrate();
        }
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals("spawn")) {
            for (String eventPlayer : playerInKeyEvent.keySet()) {
                int value = playerInKeyEvent.get(eventPlayer);
                if (value > 0) {
                    Player target = Bukkit.getPlayer(eventPlayer);
                    if (!player.getName().equals(target.getName())) {
                        if (player.getLocation().distance(target.getLocation()) <= 4) {
                            Vector kb = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(1);
                            player.setVelocity(kb);
                            player.sendMessage(getPmTTC("&cVocê não pode chegar próximo de um player que está abrindo uma crate"));
                        }
                    }
                }
            }
        }
    }

    private final Plugin plugin;

    public CrateEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Interact(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack hand = player.getItemInHand();
        Block block = event.getClickedBlock();
        if (hand.getTypeId() == 11822 && action == Action.RIGHT_CLICK_BLOCK && block.getTypeId() == 2304) {
            if (hand.getItemMeta().hasLore()) {
                if (hand.getItemMeta().getLore().get(1).contains("Key:")) {
                    event.setCancelled(true);
                    String keyLore = hand.getItemMeta().getLore().get(1);
                    boolean exist = false;
                    for (ItemStack item : invHubCrate.getContents()) {
                        if (item != null && item.getItemMeta().hasLore()) {
                            if (item.getItemMeta().getLore().get(1).equals(keyLore)) {
                                exist = true;
                                break;
                            }
                        }
                    }
                    if (!exist) {
                        player.sendMessage(getPmTTC("&cOpa, parece que esta não key existe no sistema de crate"));
                    }
                    if (exist) {
                        CrateKey crateKey = new CrateKey(plugin);
                        String key = keyLore.split(" ")[1];
                        String[] listChances = crateKey.getRandomItem(key);
                        if (listChances.length == 0) {
                            player.sendMessage(getPmTTC("&cOpa, parece que esta key não tem itens selecionados para ganhar"));
                            return;
                        }
                        int value = 0;
                        if (playerInKeyEvent.containsKey(player.getName())) {
                            value = playerInKeyEvent.get(player.getName());
                        }
                        playerInKeyEvent.put(player.getName(), value + 1);
                        player.openInventory(crateKey.getInvRandomItem(key, player));
                        hand.setAmount(hand.getAmount() - 1);
                        player.setItemInHand(hand);
                    }
                }
            }
        }
    }
}
