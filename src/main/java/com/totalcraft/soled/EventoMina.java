package com.totalcraft.soled;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.totalcraft.soled.Utils.getDelayTps;
import static com.totalcraft.soled.prefixMsgs.*;

public class EventoMina extends JavaPlugin implements Listener {
    Utils utils = new Utils();
    public static boolean eventoAtivo;
    public boolean eventoStop;
    public int timeLeft = 120;
    public int minaDuration = 5;
    public Location locationEvento = new Location(Bukkit.getWorld(Configs.worldLocatinaMina), Configs.xLocatinaMina, Configs.yLocatinaMina, Configs.zLocatinaMina);
    static HashMap<String, Location> playerLocations = new HashMap<>();
    public static List<String> minaPlayers = new ArrayList<>();
    public List<Player> mPlayers = new ArrayList<>();
    static ItemStack pickaxe = EventoMinaUtils.createPickaxe(5, 3, 3);
    Material vajra = Material.getMaterial(30477);
    Material godPick = Material.getMaterial(4386);
    Material godStaff = Material.getMaterial(4388);
    int vTimeLeft = timeLeft;
    int vMinaDuration = minaDuration;
    private BukkitTask questionTask;
    private boolean bQuestionTask;
    private BukkitTask tpsTask;
    int question = 10;
    private BukkitTask verificationPlayer;
    private final Plugin plugin;

    public EventoMina(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mina")) {
            switch (args.length > 0 ? args[0].toLowerCase() : "") {
                case "start":
                case "stop":
                case "reset":
                case "entrar":
                case "sair":
                case "stats":
                    break;
                default:
                    sender.sendMessage(getCommandsMina(sender));
                    break;
            }
        }
        if (command.getName().equalsIgnoreCase("mina") && args.length > 0 && args[0].equalsIgnoreCase("start")) {
            if (utils.getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }

            if (bQuestionTask) {
                sender.sendMessage(getPmTTC("&cVocê já iniciou um Evento Mina que irá começar em " + question + " Segundos"));
                return true;
            }

            if (eventoAtivo) {
                sender.sendMessage(getPmTTC("&cEngração tu né? 2 Eventos ao mesmo tempo?"));
                return true;
            }

            if (args.length > 1) {
                vTimeLeft = Integer.parseInt(args[1]);
            }
            if (args.length > 2) {
                vMinaDuration = Integer.parseInt(args[2]);
            }

            if (!(vTimeLeft % 30 == 0)) {
                sender.sendMessage(getPmTTC("&cO tempo para iniciar Mina deve ser múltiplo de 30"));
                return true;
            }

            if ((vTimeLeft == 0) || (vMinaDuration == 0)) {
                sender.sendMessage(getPmTTC("&cNenhum dos dois valores pode ser 0 BOBÃO"));
                return true;
            }
            tpsTask = new Utils.TpsTask().runTaskTimer(plugin, 1L, 1L);
            sender.sendMessage(getPmTTC("Evento Mina irá iniciar em " + question + " Segundos"));
            sender.sendMessage("");
            sender.sendMessage(getPmTTC("Tempo para iniciar Mina foi setado para &b" + vTimeLeft + " Segundos"));
            sender.sendMessage(getPmTTC("Tempo de duração da Mina foi setado para &b" + vMinaDuration + " Minutos"));
            sender.sendMessage("");
            sender.sendMessage(getPmTTC("&cSe deseja mudar os valores use /mina stop e veja /mina"));
            bQuestionTask = true;
            questionTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (question <= 0) {
                    eventoAtivo = true;
                    startEventomina();
                    question = 10;
                    bQuestionTask = false;
                    questionTask.cancel();
                    return;
                } else if (eventoStop) {
                    sender.sendMessage("");
                    sender.sendMessage(getPmTTC("&cEvento mina parado"));
                    sender.sendMessage("");
                    question = 10;
                    eventoStop = false;
                    bQuestionTask = false;
                    questionTask.cancel();
                    tpsTask.cancel();
                    return;
                }
                question--;
            }, 20L, 20L);
        }
        if (command.getName().
                equalsIgnoreCase("mina") && args.length > 0 && args[0].
                equalsIgnoreCase("entrar")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            Player player = (Player) sender;
            if (!eventoAtivo) {
                sender.sendMessage(getPmTTC("&cO Evento Mina não está ocorrendo !"));
                return true;
            }
            if (utils.getAdm(sender)) {
                for (ItemStack item : player.getInventory().getContents()) {
                    for (ItemStack armor : player.getInventory().getArmorContents()) {
                        if (item != null && item.getType() != Material.AIR || armor != null && armor.getType() != Material.AIR) {
                            sender.sendMessage(getPmTTC("&cVocê não pode entrar no Evento Mina com itens no inventário"));
                            return true;
                        }
                    }
                }
            }
            minaPlayers.add(player.getName());
            mPlayers.add(player);
            playerLocations.put(player.getName(), player.getLocation());
            player.teleport(locationEvento);

            player.getInventory().addItem(pickaxe);
            player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

            verificationPlayer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (!eventoAtivo) {
                    verificationPlayer.cancel();
                    return;
                }
                Iterator<Player> it = mPlayers.iterator();
                while (it.hasNext()) {
                    Player minaPlayer = it.next();
                    if (minaPlayer == null) {
                        continue;
                    }
                    PermissionUser user = PermissionsEx.getUser(minaPlayer);
                    if (minaPlayer.isFlying() && !user.has("ttcplugin.admin")) {
                        minaPlayer.setFlying(false);
                        minaPlayer.sendMessage(getPmTTC("&cSeu fly foi desativado no evento"));
                    }
                    for (ItemStack item : minaPlayer.getInventory().getContents()) {
                        if (item == null || (item.getType() != vajra && item.getType() != godPick && item.getType() != godStaff)) {
                            continue;
                        }
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            Bukkit.getServer().dispatchCommand(minaPlayer, "mina sair");
                            it.remove();
                        });
                        Bukkit.broadcastMessage(getPmTTC(minaPlayer.getName() + " &Ctentou usar itens errados no Evento Mina Rs"));
                        break;
                    }
                }
            }, 100L, 40L);
        }

        if (command.getName().
                equalsIgnoreCase("mina") && args.length > 0 && args[0].
                equalsIgnoreCase("stop")) {
            if (utils.getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }
            if (!eventoAtivo && !bQuestionTask) {
                sender.sendMessage(getPmTTC("&cEvento mina não está ocorrendo, Bobão"));
                return true;
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(getPmTTC("&lO Evento Mina foi para por um ADM"));
            Bukkit.broadcastMessage("");
            eventoStop();
        }
        if (command.getName().

                equalsIgnoreCase("mina") && args.length > 0 && args[0].

                equalsIgnoreCase("sair")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(getPmConsole());
                return true;
            }
            if (!eventoAtivo) {
                sender.sendMessage(getPmTTC("&cEntão tu quer sair do evento que não está ocorrendo?"));
                return true;
            }
            Player player = (Player) sender;
            PlayerInventory inventory = player.getInventory();
            if (!minaPlayers.contains(player.getName())) {
                sender.sendMessage(getPmTTC("&cQuer sair do evento que você não está ???"));
                return true;
            }
            inventory.remove(Material.COOKED_BEEF);
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == pickaxe.getType()) {
                    inventory.clear(i);
                }
            }
            sender.sendMessage(getPmTTC("&fVocê saiu do Evento Mina"));
            minaPlayers.remove(player.getName());
            mPlayers.remove(player);

            player.teleport(playerLocations.get(player.getName()));
        }
        if (command.getName().
                equalsIgnoreCase("mina") && args.length > 0 && args[0].
                equalsIgnoreCase("reset")) {
            if (utils.getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }
            EventoMinaUtils.placeRandomBlocks();
            sender.sendMessage(getPmTTC("&f&lMina Resetada"));
        }
        if (command.getName().
                equalsIgnoreCase("mina") && args.length > 0 && args[0].
                equalsIgnoreCase("stats")) {

            if (utils.getAdm(sender)) {
                sender.sendMessage(getPmNotAdm());
                return true;
            }
            if (!eventoAtivo) {
                sender.sendMessage(getPmTTC("&cO evento não está ocorrendo"));
                return true;

            }
            DecimalFormat df = new DecimalFormat("#.##");
            double tps = 20 / Utils.tps;
            sender.sendMessage(getPmTTC("&bStatus do Evento Mina\n" +
                    "\n&eJogadores no Evento: &a" + minaPlayers.size() +
                    "\n&eVelocidade dos Delay: &a" + df.format(tps) + "x" +
                    "\n&eTps do Server: &a" + Utils.tps));
        }
        return true;
    }

    private BukkitTask eventoMinaTask;
    private BukkitTask startMinaTask;

    public void startEventomina() {
        startMinaTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (vTimeLeft <= 0) {
                eventoMina();
                vMinaDuration--;
                startMinaTask.cancel();
                return;
            }
            if (!eventoAtivo) {
                startMinaTask.cancel();
                return;
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Recheada Irá Iniciar !!!"));
            Bukkit.broadcastMessage(getPmTTC("&lDigite &b&l/mina entrar &f&lPara entrar"));
            Bukkit.broadcastMessage(getPmTTC("&c&lNão é permitido &f&lentrada de itens no evento"));
            Bukkit.broadcastMessage(getPmTTC("&lVocês ganharam os Kit Automaticamente"));
            Bukkit.broadcastMessage(getPmTTC("&b&l" + vTimeLeft + " Segundos &f&lpara o evento iniciar"));
            Bukkit.broadcastMessage(getPmTTC("&lA Mina terá &b&l" + vMinaDuration + " Minutos &f&lde duração"));
            Bukkit.broadcastMessage("");

            vTimeLeft -= 30;
        }, 0L, getDelayTps(20 * 30));
    }

    public void eventoMina() {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "rg flag mina build -w spawn allow");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Iniciado !!!"));
        Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Iniciado !!!"));
        Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Iniciado !!!"));
        Bukkit.broadcastMessage("");

        eventoMinaTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (vMinaDuration <= 0) {

                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Finalizado !!!"));
                Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Finalizado !!!"));
                Bukkit.broadcastMessage(getPmTTC("&lEvento Mina Finalizado !!!"));
                Bukkit.broadcastMessage("");

                eventoStop();
            }
            if (!eventoAtivo) {
                eventoMinaTask.cancel();
                return;
            }

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(getPmTTC((vMinaDuration == 1 ? "&f&lFalta " : "&f&lFaltam ") + vMinaDuration + (vMinaDuration == 1 ? " &b&lMinuto " : " &b&lMinutos ") + "&f&lPara"));
            Bukkit.broadcastMessage(getPmTTC("&lTerminar o Evento Mina !!!"));
            Bukkit.broadcastMessage(getPmTTC("&lUtilize &b&l/mina entrar &f&lPara entrar"));
            Bukkit.broadcastMessage("");

            vMinaDuration--;
        }, getDelayTps(20 * 60), getDelayTps(20 * 60));
    }

    public void eventoStop() {
        eventoStop = true;
        if (eventoAtivo) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "rg flag mina build -w spawn deny");

            for (Player player : mPlayers) {
                player.teleport(playerLocations.get(player.getName()));
                PlayerInventory inventory = player.getInventory();
                inventory.remove(Material.COOKED_BEEF);
                for (int i = 0; i < inventory.getSize(); i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item != null && item.getType() == pickaxe.getType()) {
                        inventory.clear(i);
                    }
                }
            }
            playerLocations.clear();
            minaPlayers.clear();
            mPlayers.clear();
            vTimeLeft = timeLeft;
            vMinaDuration = minaDuration;
            eventoAtivo = false;
            EventoMinaUtils.placeRandomBlocks();
            if (eventoMinaTask != null) {
                eventoMinaTask.cancel();
            }
            if (startMinaTask != null) {
                startMinaTask.cancel();
            }
            tpsTask.cancel();
            eventoStop = false;
        }
    }
}