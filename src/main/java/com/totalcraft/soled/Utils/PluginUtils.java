package com.totalcraft.soled.Utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginUtils {
    public static Plugin getPluginByName(String name) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            System.out.println(plugin.getName());
            if (name.equalsIgnoreCase(plugin.getName())) {
                return plugin;
            }
        }
        return null;
    }

    public void enable(Plugin plugin) {
        if (plugin != null && !plugin.isEnabled()) {
            Bukkit.getPluginManager().enablePlugin(plugin);
        }
    }

    public void disable(Plugin plugin) {
        if (plugin != null && plugin.isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private String load(Plugin plugin) {
        return load(plugin.getName());
    }

    public static String load(String name) {
        Plugin target;
        File pluginDir = new File("plugins");
        if (!pluginDir.isDirectory()) {
            return "Não encontrado Plugin";
        }
        File pluginFile = new File(pluginDir, name + ".jar");
        if (!pluginFile.isFile()) {
            for (File f : Objects.requireNonNull(pluginDir.listFiles())) {
                if (f.getName().endsWith(".jar")) {
                    try {
                        JarFile jarFile = new JarFile(f);
                        JarEntry entry = jarFile.getJarEntry("plugin.yml");

//                        if (entry != null) {
//                            InputStream inputStream = jarFile.getInputStream(entry);
//                            YamlConfiguration yamlConfig = new YamlConfiguration();
//                            yamlConfig.load(String.valueOf(new InputStreamReader(inputStream)));
//
//                            String pluginName = yamlConfig.getString("name");
//                            if (pluginName != null && pluginName.equalsIgnoreCase(name)) {
//                                pluginFile = f;
//                                break;
//                            }
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Plugin lodado com problemas";
                    }
                }
            }
        }

        try {
            target = Bukkit.getPluginManager().loadPlugin(pluginFile);
        } catch (InvalidDescriptionException | InvalidPluginException e) {
            e.printStackTrace();
            return "Plugin lodado com problemas";
        }

        target.onLoad();
        Bukkit.getPluginManager().enablePlugin(target);
        return "Plugin lodado";
    }


    public static void reloadPlugin(Plugin plugin, CommandSender sender) {
        sender.sendMessage("c");
        if (plugin != null) {
            sender.sendMessage(unload(plugin));
            sender.sendMessage("a");
            sender.sendMessage(load(plugin.getName()));
            sender.sendMessage("b");
        }
    }

    public static String unload(Plugin plugin) {
        String name = plugin.getName();
        org.bukkit.plugin.PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadListeners = true;

        Map<String, Plugin> plugins = null;
        if (pluginManager != null) {
            pluginManager.disablePlugin(plugin);
//            try {
//                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
//                pluginsField.setAccessible(true);
//                plugins = (Map<String, Plugin>) pluginsField.get(pluginManager);
//
//                Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
//                lookupNamesField.setAccessible(true);
//                names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
//
//                try {
//                    Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
//                    listenersField.setAccessible(true);
//                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
//                } catch (Throwable e) {
//                    reloadListeners = false;
//                }
//
//                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
//                commandMapField.setAccessible(true);
//                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
//
//                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
//                knownCommandsField.setAccessible(true);
//                commands = (Map<String, Command>) knownCommandsField.get(commandMap);
//            } catch (NoSuchFieldException e) {
//                return LangConfig.generalPluginUnloadProblems;
//            } catch (IllegalAccessException e) {
//                return LangConfig.generalPluginUnloadProblems;
//            }
        }

        pluginManager.disablePlugin(plugin);

        if (plugins != null && plugins.containsKey(name)) {
            plugins.remove(name);
        }
        if (names != null && names.containsKey(name)) {
            names.remove(name);
        }
        if (listeners != null && reloadListeners) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                for (SortedSet<RegisteredListener> registeredListeners : listeners.values()) {
                    for (RegisteredListener value : registeredListeners) {
                        if (value.getPlugin() == plugin) {
                            set.remove(value);
                        }
                    }
                }
            }
        }
        if (commandMap != null) {
            for (Map.Entry<String, Command> entry : commands.entrySet()) {
                String key = entry.getKey();
                Command value = entry.getValue();
                if (value instanceof PluginCommand && ((PluginCommand) value).getPlugin() == plugin) {
                    ((PluginCommand) value).unregister(commandMap);
                    commands.remove(key);
                }
            }
        }

        ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            try {
                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);
                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);
            } catch (NoSuchFieldException | IllegalArgumentException | SecurityException |
                     IllegalAccessException ignored) {
            }
        }
        System.gc();
        return "Sucesso em unload";
    }
}
