package com.totalcraft.soled;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;

import static com.totalcraft.soled.Main.permissionAdm;
import static com.totalcraft.soled.prefixMsgs.*;

public class EventoMina extends JavaPlugin implements Listener {
    EventoMinaUtils eventoMinaUtils = new EventoMinaUtils();
    public static boolean eventoAtivo = false;
    public static boolean eventoStop = false;
    public static Location locationEvento = new Location(Bukkit.getWorld("world"), 0, 64, 0);
    private final Plugin plugin;
    Location playerLocation;
    public static int timeLeft = 120;
    public static int minaDuration = 5;
    int vTimeLeft = timeLeft;
    int vMinaDuration = minaDuration;
    List<Player> minaPlayers = new ArrayList<>();
    ItemStack pickaxe = createPickaxe(5, 3, 3);
    private BukkitTask questionTask;
    private boolean bQuestionTask = false;
    int question = 20;

    public EventoMina(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mina") && args.length > 0 && args[0].equalsIgnoreCase("start")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);
                if (!user.has(permissionAdm)) {
                    player.sendMessage(getNotAdm());
                    return true;
                }
            }
            if (bQuestionTask) {
                sender.sendMessage(getTTCMessage("&cVocê já iniciou um Evento Mina que irá começar em " + question + " Segundos"));
                return true;
            }

            if (eventoAtivo) {
                sender.sendMessage(getTTCMessage("&cEngração tu né? 2 Eventos ao mesmo tempo?"));
                return true;
            }

            if (args.length > 1) {
                vTimeLeft = Integer.parseInt(args[1]);
            }
            if (args.length > 2) {
                vMinaDuration = Integer.parseInt(args[2]);
            }

            if (!(vTimeLeft % 30 == 0)) {
                sender.sendMessage(getTTCMessage("&cO tempo para iniciar Mina deve ser múltiplo de 30"));
                return true;
            }

            if ((vTimeLeft == 0) || (vMinaDuration == 0)) {
                sender.sendMessage(getTTCMessage("&cNenhum dos dois valores pode ser 0 BOBÃO"));
                return true;
            }

            sender.sendMessage(getTTCMessage("Evento Mina irá iniciar em " + question + " Segundos"));
            sender.sendMessage("");
            sender.sendMessage(getTTCMessage("Tempo para iniciar Mina foi setado para &b" + vTimeLeft + " Segundos"));
            sender.sendMessage(getTTCMessage("Tempo de duração da Mina foi setado para &b" + vMinaDuration + " Minutos"));
            sender.sendMessage("");
            sender.sendMessage(getTTCMessage("&cSe deseja mudar os valores use /mina stop e veja /mina"));

            bQuestionTask = true;
            questionTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (question <= 0) {
                    eventoAtivo = true;
                    startEventomina();
                    question = 20;
                    bQuestionTask = false;
                    questionTask.cancel();
                    return;
                } else if (eventoStop) {
                    sender.sendMessage("");
                    sender.sendMessage(getTTCMessage("&cEvento mina parado"));
                    sender.sendMessage("");
                    question = 20;
                    eventoStop = false;
                    bQuestionTask = false;
                    questionTask.cancel();
                    return;
                }
                question--;
            }, 0L, 20L);
        }
        if (command.getName().equalsIgnoreCase("mina") && args.length > 0 && args[0].equalsIgnoreCase("entrar")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getConsoleMessage());
                return true;
            }
            Player player = (Player) sender;
            if (!eventoAtivo) {
                sender.sendMessage(getTTCMessage("&cO Evento Mina não está ocorrendo !"));
                return true;
            }
            for (ItemStack item : player.getInventory().getContents()) {
                for (ItemStack armor : player.getInventory().getArmorContents()) {
                    if (item != null && item.getType() != Material.AIR || armor != null && armor.getType() != Material.AIR) {
                        sender.sendMessage(getTTCMessage("&cVocê não pode entrar no Evento Mina com itens no inventário"));
                        return true;
                    }
                }
            }

            minaPlayers.add(player);
            playerLocation = player.getLocation();
            player.teleport(locationEvento);

            player.getInventory().addItem(pickaxe);
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

        }
        if (command.getName().equalsIgnoreCase("mina") && args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);
                if (!user.has(permissionAdm)) {
                    player.sendMessage(getNotAdm());
                    return true;
                }
            }
            if (!eventoAtivo && !bQuestionTask) {
                sender.sendMessage(getTTCMessage("&cEvento mina não está ocorrendo, Bobão"));
                return true;
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina foi parado por um ADM"));
            Bukkit.broadcastMessage("");
            eventoStop();
        }
        if (command.getName().equalsIgnoreCase("mina") && args.length > 0 && args[0].equalsIgnoreCase("sair")) {
            if (!eventoAtivo) {
                sender.sendMessage(getTTCMessage("&cEntão tu quer sair do evento que não está ocorrendo?"));
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(getConsoleMessage());
                return true;
            }
            Player player = (Player) sender;
            PlayerInventory inventory = player.getInventory();
            if (!minaPlayers.contains(player)) {
                sender.sendMessage(getTTCMessage("&cQuer sair do evento que você não está ???"));
                return true;
            }
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == pickaxe.getType()) {
                    inventory.clear(i);
                }
            }
            sender.sendMessage(getTTCMessage("&fVocê saiu do Evento Mina"));
            minaPlayers.remove(player);
            player.teleport(playerLocation);
        }
        if (command.getName().equalsIgnoreCase("mina") && args.length > 0 && args[0].equalsIgnoreCase("reset")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PermissionUser user = PermissionsEx.getUser(player);
                if (!user.has(permissionAdm)) {
                    player.sendMessage(getNotAdm());
                    return true;
                }
            }
            eventoMinaUtils.placeRandomBlocks();
            sender.sendMessage(getTTCMessage("&f&lMina Resetada"));
        }

        if (command.getName().equalsIgnoreCase("mina")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "start":
                case "stop":
                case "reset":
                case "entrar":
                case "sair":
                    break;
                default:
                    sender.sendMessage(getCommandsMina(sender));
                    break;
            }
        }
        return true;
    }

    private BukkitTask eventoMinaTask;
    private BukkitTask startMinaTask;

    public void startEventomina() {
        startMinaTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (vTimeLeft <= 0) {
                eventoMina();
                startMinaTask.cancel();
                return;
            }
            if (!eventoAtivo) {
                startMinaTask.cancel();
                return;
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Recheada Irá Iniciar !!!"));
            Bukkit.broadcastMessage(getTTCMessage("&lDigite &b&l/mina entrar &f&lPara entrar"));
            Bukkit.broadcastMessage(getTTCMessage("&c&lNão é permitido &f&lentrada de itens no evento"));
            Bukkit.broadcastMessage(getTTCMessage("&lVocês ganharam os Kit Automaticamente"));
            Bukkit.broadcastMessage(getTTCMessage("&b&l" + vTimeLeft + " Segundos &f&lpara o evento iniciar"));
            Bukkit.broadcastMessage(getTTCMessage("&lA Mina terá &b&l" + vMinaDuration + " Minutos &f&lde duração"));
            Bukkit.broadcastMessage("");

            vTimeLeft -= 30;
        }, 0L, 20L * 30);
    }

    public void eventoMina() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "say Testado");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Iniciado !!!"));
        Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Iniciado !!!"));
        Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Iniciado !!!"));
        Bukkit.broadcastMessage("");

        eventoMinaTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (vMinaDuration <= 0) {

                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Finalizado !!!"));
                Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Finalizado !!!"));
                Bukkit.broadcastMessage(getTTCMessage("&lEvento Mina Finalizado !!!"));
                Bukkit.broadcastMessage("");

                eventoStop();
            }
            if (!eventoAtivo) {
                eventoMinaTask.cancel();
                return;
            }

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(getTTCMessage((vMinaDuration == 1 ? "&f&lFalta " : "&f&lFaltam ") + vMinaDuration + (vMinaDuration == 1 ? " &b&lMinuto " : " &b&lMinutos ") + "&f&lPara"));
            Bukkit.broadcastMessage(getTTCMessage("&lTerminar o Evento Mina !!!"));
            Bukkit.broadcastMessage(getTTCMessage("&lUtilize &b&l/mina entrar &f&lPara entrar"));
            Bukkit.broadcastMessage("");

            vMinaDuration--;
        }, 20L * 10, 20L * 10);
    }

    public void eventoStop() {
        eventoStop = true;
        if (eventoAtivo) {
            for (Player player : minaPlayers) {
                PlayerInventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item != null && item.getType() == pickaxe.getType()) {
                        inventory.clear(i);
                    }
                }
                player.teleport(playerLocation);
            }
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "say Testado");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "say Testado");
            minaPlayers.clear();
            vTimeLeft = timeLeft;
            vMinaDuration = minaDuration;
            eventoAtivo = false;
            if (eventoMinaTask != null) {
                eventoMinaTask.cancel();
            }
            if (startMinaTask != null) {
                startMinaTask.cancel();
            }
            eventoStop = false;
        }
    }

    public ItemStack createPickaxe(int Efficieny, int Fortune, int Unbreaking) {
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();

        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, Efficieny, true);
        pickaxeMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, Fortune, true);
        pickaxeMeta.addEnchant(Enchantment.DURABILITY, Unbreaking, true);

        pickaxe.setItemMeta(pickaxeMeta);
        return pickaxe;
    }
}