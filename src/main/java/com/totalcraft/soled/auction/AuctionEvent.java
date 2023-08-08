package com.totalcraft.soled.auction;

import com.totalcraft.soled.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.totalcraft.soled.Commands.Vender.formatter;
import static com.totalcraft.soled.Utils.PrefixMsgs.getFormatColor;
import static com.totalcraft.soled.Utils.PrefixMsgs.getPmTTC;
import static com.totalcraft.soled.auction.AuctionBase.*;
import static com.totalcraft.soled.auction.AuctionLog.saveAuctionLog;
import static com.totalcraft.soled.auction.ConfigAuction.*;
import static com.totalcraft.soled.auction.Hub.*;
import static com.totalcraft.soled.auction.PlayerAuction.*;
import static com.totalcraft.soled.auction.SetItemAuction.*;

public class AuctionEvent implements Listener {
    private final Main main;

    public AuctionEvent(Main main) {
        this.main = main;
    }

    List<String> blockedItems = Arrays.asList("14277:*" , "14282:*", "14283:*", "14284:*", "14286:*", "14287:*", "14288:*", "14289:*", "14290:*", "14291:*", "14292:*", "14293:*", "14294:*", "14295:*", "14296:*", "14297:*", "14298:*", "14299:*", "14300:*", "14301:*", "14302:*", "14303:*", "14305:*", "14306:*", "14307:*", "14308:*", "14309:*", "14311:*", "14312:*", "14313:*", "14314:*", "14315:*", "14316:*", "14317:*", "14319:*", "14321:*", "14322:*", "14323:*", "14324:*", "14325:*", "14326:*", "14327:*", "14328:*", "14329:*", "4361:*", "30765:*", "30766:*", "30767:0", "30768:0", "30769:*", "30770:*", "30771:*", "30772:*", "30773:*", "30774:*", "30775:*", "30776:*", "30777:*", "30778:*", "30779:*", "30780:*", "30781:*", "30782:*", "30790:*", "30764:*", "30760:*", "30761:*", "30762:*", "30763:*", "30756:*", "30757:*", "30758:*", "30759:*", "25265:*");
    Map<String, ItemStack> playerSell = new HashMap<>();
    Map<String, ItemStack> playerAuc = new HashMap<>();
    Map<String, ItemStack> playerShop = new HashMap<>();
    String invSell = ChatColor.AQUA + "Venda: ";
    String invAuction = ChatColor.AQUA + "Leilão";
    String invAucPlayer = ChatColor.BLACK + "Administrar o seu leilão";
    String invBuyItem = ChatColor.BLACK + "Item: " + ChatColor.GOLD;

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancel(InventoryClickEvent event) {
        for (String list : invNames) {
            if (event.getInventory().getName().contains(list)) {
                if (event.getHotbarButton() >= 0 && event.getHotbarButton() <= 8) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void inventory(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack item = event.getCurrentItem();
        if (inventory == null || item == null || item.getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String nameItem = null;
        for (String list : invNames) {
            if (event.getInventory().getName().contains(list)) {
                event.setCancelled(true);
            }
        }
        ConfigAuction configAuction = new ConfigAuction(main);
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().hasDisplayName()) {
                    nameItem = item.getItemMeta().getDisplayName();
                    if (nameItem.equals(closeInv.getItemMeta().getDisplayName()) || nameItem.equals(negativeHub.getItemMeta().getDisplayName()) || nameItem.equals(negativePlayerAuc.getItemMeta().getDisplayName()) || nameItem.equals(negativeAuc.getItemMeta().getDisplayName())) {
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                    if (nameItem.equals(hubInv.getItemMeta().getDisplayName())) {
                        player.closeInventory();
                        player.openInventory(hubAuction);
                    }
                }
            }
        }

        if (inventory.getName().equals(hubAuction.getName())) {
            if (nameItem != null) {
                if (nameItem.equals(openSellItem.getItemMeta().getDisplayName())) {
                    if (limitSetItem(player)) {
                        player.sendMessage(getPmTTC("&cOpa amigo, parece que você atingiu o máximo de vendas no leilão"));
                        player.closeInventory();
                        return;
                    }
                }
                openHubs(player, nameItem);
            }
        }

        if (inventory.getName().contains(invSell)) {
            for (String s : blockedItems) {
                String[] idItem = s.split(":");
                int id = Integer.parseInt(idItem[0]);
                if (idItem[1].equals("*")) {
                    if (id == item.getTypeId()) {
                        player.sendMessage(getPmTTC("&cEste item não pode ser leiloado."));
                        return;
                    }
                } else {
                    short meta = Short.parseShort(idItem[1]);
                    if (id == item.getTypeId() && meta == item.getDurability()) {
                        player.sendMessage(getPmTTC("&cEste item não pode ser leiloado."));
                        return;
                    }
                }
            }
            if (nameItem != null) {
                if (!itemSell(item)) {
                    if (!playerSell.containsKey(player.getName())) {
                        setItemAuction(player, item);
                        playerSell.put(player.getName() + "-false", item);
                    }
                }
                if (playerSell.containsKey(player.getName() + "-false")) {
                    if (nameItem.equals(affirmativeHub.getItemMeta().getDisplayName())) {
                        ItemStack kk = playerSell.get(player.getName() + "-false");
                        playerSell.put(player.getName() + "-true", kk);
                        player.closeInventory();
                        player.sendMessage(getPmTTC("&bDigite o valor do item"));
                    }
                }
            } else {
                if (!playerSell.containsKey(player.getName())) {
                    setItemAuction(player, item);
                    playerSell.put(player.getName() + "-false", item);
                }
            }
        }

        if (inventory.getName().contains(invAuction)) {
            if (!playerShop.containsKey(player.getName())) {
                if (item.getItemMeta().hasLore()) {
                    if (item.getItemMeta().getLore().get(0).contains("Vendedor:")) {
                        playerShop.put(player.getName(), item);
                    }
                }
            }
            if (playerShop.containsKey(player.getName())) {
                questionAuction(player, playerShop.get(player.getName()));
            }
            if (nameItem != null) {
                String[] part = inventory.getName().split(" ");
                int index = Integer.parseInt(part[1]);
                if (nameItem.equals(nextAuction.getItemMeta().getDisplayName())) {
                    if (index >= 0 && index < AuctionItems.size()) {
                        player.openInventory(AuctionItems.get(index));
                    }
                }
                if (nameItem.equals(previousAuction.getItemMeta().getDisplayName())) {
                    player.openInventory(AuctionItems.get(index - 2));
                }
            }
        }

        if (inventory.getName().contains(invBuyItem)) {
            if (nameItem != null) {
                if (nameItem.equals(affirmativeAuc.getItemMeta().getDisplayName())) {
                    ItemStack shopItem = playerShop.get(player.getName());
                    String partValor = shopItem.getItemMeta().getLore().get(1).split(" ")[2];
                    String nameSeller = shopItem.getItemMeta().getLore().get(0).split(" ")[1];
                    long valor = Long.parseLong(partValor);
                    if (nameSeller.equals(player.getName())) {
                        player.sendMessage(getPmTTC("&cOpa amigão, não dá para comprar seu próprio item né"));
                        player.closeInventory();
                        return;
                    }
                    Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
                    if (!economy.has(player.getName(), valor)) {
                        player.sendMessage(getPmTTC("&cVocê não tem dinheiro suficiente para comprar este item"));
                        return;
                    }
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(getPmTTC("&cVocê não tem espaço no invetário para receber este item"));
                        return;
                    }
                    if (!containsItem(shopItem)) {
                        player.sendMessage(getPmTTC("&cOpa, parece que item já foi comprado"));
                        player.closeInventory();
                        return;
                    }
                    economy.withdrawPlayer(player.getName(), valor);
                    economy.depositPlayer(nameSeller, valor);
                    ItemMeta itemMeta = shopItem.getItemMeta();
                    itemMeta.setLore(null);
                    shopItem.setItemMeta(itemMeta);
                    player.getInventory().addItem(shopItem);
                    removeItemAuction(shopItem);
                    playerShop.remove(player.getName());
                    player.closeInventory();
                    String valorFormat = String.format("%s", formatter.format(valor));
                    player.sendMessage(getPmTTC("&eVocê comprou o item &b" + shopItem.getType().name() + " &eno valor de &aR$ " + valorFormat + " &edo jogador &b" + nameSeller));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                    saveAuctionLog(getFormatDate(player) + " comprou o item " + shopItem.getType().name() + " do jogador " + nameSeller + " por R$ " + valorFormat);
                    Player seller = Bukkit.getPlayer(nameSeller);
                    configAuction.reloadAuctions();
                    if (seller != null) {
                        seller.playSound(seller.getLocation(), Sound.LEVEL_UP, 1, 1);
                        seller.sendMessage(getPmTTC("&eO jogador &b" + player.getName() + " &ecomprou o item &b" + shopItem.getType().name() + " &eno valor de &aR$ " + valorFormat + " &eno seu leilão"));
                    }
                    if (valor >= 50000000) {
                        Bukkit.broadcastMessage(getPmTTC("&b" + player.getName() + " &eComprou o item &b" + shopItem.getType().name() + " &eno /leilao por &aR$ " + valorFormat + " !!!"));
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1);
                        }
                    }
                }
            }
        }

        if (inventory.getName().equals(invAucPlayer)) {
            if (item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().contains("Informação:")) return;
            }
            questionRemoveItem(player, item);
            if (!playerAuc.containsKey(player.getName())) {
                if (item.getItemMeta().hasLore()) {
                    playerAuc.put(player.getName(), item);
                }
            }

            if (nameItem != null) {
                if (playerAuc.containsKey(player.getName())) {
                    if (nameItem.equals(affirmativePlayerAuc.getItemMeta().getDisplayName())) {
                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(getPmTTC("&cVocê não tem espaço no inventário para receber este item"));
                            player.closeInventory();
                        } else {
                            removeItemAuction(playerAuc.get(player.getName()));
                            ItemStack newItem = playerAuc.get(player.getName());
                            ItemMeta meta = newItem.getItemMeta();
                            meta.setLore(null);
                            newItem.setItemMeta(meta);
                            player.getInventory().addItem(newItem);
                            player.closeInventory();
                            playerAuc.remove(player.getName());
                            player.sendMessage(getPmTTC("&eVocê removeu &b" + newItem.getType().name() + " &edo leilão"));
                            saveAuctionLog(getFormatDate(player) + " removeu o item " + newItem.getType().name() + " do leilão");
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                            configAuction.reloadAuctions();
                        }
                    }
                    if (nameItem.equals(changePrice.getItemMeta().getDisplayName())) {
                        player.sendMessage(getPmTTC("&bDigite o novo valor do item"));
                        ItemStack kk = playerAuc.get(player.getName());
                        playerAuc.put(player.getName() + "-price", kk);
                        player.closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void CloseInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();
        String invBuyItem = ChatColor.BLACK + "Item: " + ChatColor.GOLD;
        if (inv != null && inv.getName() != null) {
            if (inv.getName().contains(invBuyItem)) {
                playerShop.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void OpenInventory(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();
        String invAucPlayer = ChatColor.BLACK + "Administrar o seu leilão";
        if (inv != null) {
            if (inv.getName() != null) {
                if (!inv.getName().contains(invAucPlayer)) {
                    playerAuc.remove(player.getName() + "-price");
                    playerAuc.remove(player.getName());
                }
            }
        }
    }

    @EventHandler
    public void Chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (playerSell.containsKey(player.getName() + "-true")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            long price;
            try {
                price = Long.parseLong(msg);
            } catch (Exception ignored) {
                player.sendMessage(getPmTTC("&cVocê digitou um valor errado ou muito alto"));
                playerSell.remove(player.getName() + "-true");
                playerSell.remove(player.getName() + "-false");
                return;
            }
            Inventory inv = player.getInventory();
            ItemStack originalItem = playerSell.get(player.getName() + "-true");
            if (getPriceMin(originalItem, price, originalItem.getAmount())) {
                String id = originalItem.getTypeId() + ":" + originalItem.getDurability();
                if (priceMinServer.get(id) == null) {
                    id = originalItem.getTypeId() + ":*";
                }
                player.sendMessage(getPmTTC("&cO valor minimo de cada 1 deste item é de R$ " + priceMinServer.get(id)));
                return;
            }
            if (price <= 0) {
                player.sendMessage(getPmTTC("&cValor deste item não pode ser menor ou igual a 0"));
                return;
            }
            if (inv.contains(originalItem)) {
                getInventory(false);
                String valorFormat = String.format("%s", formatter.format(price));
                player.sendMessage("");
                player.sendMessage(getPmTTC("&eVocê leiloou &b" + originalItem.getType().name() + " &ePor &aR$ " + valorFormat));
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
                saveAuctionLog(getFormatDate(player) + " leiloou o item " + originalItem.getType().name() + " por R$ " + valorFormat);
                ItemStack newItem = new ItemStack(originalItem.getType(), originalItem.getAmount(), originalItem.getDurability());
                newItem.setItemMeta(originalItem.getItemMeta());
                ItemMeta itemMeta = newItem.getItemMeta();
                List<String> lore = Arrays.asList(getFormatColor("&eVendedor:&b " + player.getName()), getFormatColor("&eValor: &aR$ " + price));
                itemMeta.setLore(lore);
                newItem.setItemMeta(itemMeta);
                addItemAuction(newItem, false);
                inv.removeItem(originalItem);
                if (price >= 50000000) {
                    Bukkit.broadcastMessage(getPmTTC("&b" + player.getName() + " &eLeiloou o item &b" + originalItem.getType().name() + " &eno /leilao por &aR$ " + valorFormat + " !!!"));
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                }
                saveAuction();
            } else {
                player.sendMessage(getPmTTC("&cO item selecionado não está mais em seu inventário !"));
            }
            playerSell.remove(player.getName() + "-true");
            playerSell.remove(player.getName() + "-false");
        }
        if (playerAuc.containsKey(player.getName() + "-price")) {
            event.setCancelled(true);
            String msg = event.getMessage();
            long price;
            try {
                price = Long.parseLong(msg);
            } catch (Exception ignored) {
                player.sendMessage(getPmTTC("&cVocê digitou um valor errado ou muito alto"));
                playerAuc.remove(player.getName() + "-price");
                playerAuc.remove(player.getName());
                return;
            }
            getInventory(false);
            ItemStack originalItem = playerAuc.get(player.getName() + "-price");
            if (getPriceMin(originalItem, price, originalItem.getAmount())) {
                String id = originalItem.getTypeId() + ":" + originalItem.getDurability();
                if (priceMinServer.get(id) == null) {
                    id = originalItem.getTypeId() + ":*";
                }
                player.sendMessage(getPmTTC("&cO valor minimo de cada 1 deste item é de R$ " + priceMinServer.get(id)));
                return;
            }
            if (price <= 0) {
                player.sendMessage(getPmTTC("&cValor deste item não pode ser menor ou igual a 0"));
                return;
            }
            ItemStack newItem = new ItemStack(originalItem.getType(), originalItem.getAmount(), originalItem.getDurability());
            newItem.setItemMeta(originalItem.getItemMeta());
            ItemMeta itemMeta = newItem.getItemMeta();
            List<String> lore = Arrays.asList(getFormatColor("&eVendedor:&b " + player.getName()), getFormatColor("&eValor: &aR$ " + price));
            itemMeta.setLore(lore);
            newItem.setItemMeta(itemMeta);
            addItemAuction(newItem, false);
            removeItemAuction(originalItem);
            String valorFormat = String.format("%s", formatter.format(price));
            player.sendMessage(getPmTTC("&eVocê alterou valor de &b" + newItem.getType().name() + " &ePara &aR$ " + valorFormat));
            saveAuctionLog(getFormatDate(player) + " alterou valor do item " + newItem.getType().name() + " para R$ " + valorFormat);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
            saveAuction();
        }
        playerAuc.remove(player.getName() + "-price");
        playerAuc.remove(player.getName());
    }

    private String getFormatDate(Player player) {
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar cal = Calendar.getInstance(tz);
        Date date = cal.getTime();
        return date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " - " + player.getName();
    }
}
