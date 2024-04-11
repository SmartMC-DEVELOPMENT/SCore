package us.smartmc.backend.instance.plugin;

import us.smartmc.smartaddons.plugin.*;
import us.smartmc.smartaddons.plugin.PluginDescription;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private final HashMap<String, AddonPlugin> plugins;
    private final HashMap<AddonPlugin, String> pluginDirs = new HashMap<>();

    public PluginLoader() {
        plugins = new HashMap<>();
    }

    public void loadPlugins(String pluginsDirectory) {
        File directory = new File(pluginsDirectory);
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            loadPlugin(file, true);
        }
    }

    public void loadPlugin(File file, boolean start) {
        if (!file.isFile() && !file.getName().endsWith(".jar")) return;
        try {
            // SET PLUGIN.JSON INPUTSTREAM
            URL jarUrl = file.toURI().toURL();
            JarFile jarFile = new JarFile(file);
            InputStream pluginIn = null;
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if(entry.getName().equals("plugin.json")) {
                    pluginIn = jarFile.getInputStream(entry);
                }
            }
            // LOADER & DESCRIPTION
            AddonClassLoader classLoader = new AddonClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());
            PluginDescription description = PluginLoaderUtils.read(pluginIn);
            if (description == null)
                throw new Exception("Not valid description found! Make sure that the plugin.json is at the main path of the .jar");
            Class<?> pluginClass = classLoader.loadClass(description.getMainClass());

            // CREATE INSTANCE
            if (!AddonPlugin.class.isAssignableFrom(pluginClass)) return;
            File dataFolder = new File(file.getParentFile() + "/" + description.getName());
            AddonPlugin plugin = (AddonPlugin) pluginClass.newInstance();

            // SUPERCLASS: SET DATA FOLDER
            Field dataFolderField = plugin.getClass().getSuperclass().getDeclaredField("dataFolder");

            dataFolderField.setAccessible(true);
            dataFolderField.set(plugin, dataFolder);
            dataFolderField.setAccessible(false);

            // READ ADDON INFO TO REGISTER PLUGIN INSTANCE & PLUGIN FILE PATH
            AddonInfo info = plugin.getClass().getDeclaredAnnotation(AddonInfo.class);
            if (info == null) throw new Exception("Addon main class doesn't contain PluginInfo annotation!");
            plugins.put(info.name(), plugin);
            pluginDirs.put(plugin, file.getAbsolutePath());
            if (start) plugin.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unload(String name) {
        AddonPlugin plugin = plugins.get(name);
        if (plugin == null) return;
        plugin.stop();
        CommandsRegistry.unregister(name);
        ListenersRegistry.unregister(plugin);
        plugins.remove(name);
    }

    public void reloadPlugin(String name) {
        AddonPlugin plugin = plugins.get(name);
        if (plugin == null) {
            return;
        }
        File file = new File(pluginDirs.get(plugin));
        unload(name);
        loadPlugin(file, true);
    }

    public AddonPlugin get(String name) {
        return plugins.get(name);
    }
}
