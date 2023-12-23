package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.listener.LobbyHandlerListeners;

import java.io.File;

public class LobbyHandler {

    private final JavaPlugin plugin;
    private final FilePluginConfig config;

    public LobbyHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = new FilePluginConfig(new File(plugin.getDataFolder(), "lobbyHandler.json")).load();
        registerConfigDefaults();
        Bukkit.getPluginManager().registerEvents(new LobbyHandlerListeners(this), plugin);
        config.save();

    }

    public boolean isDisabled(String path) {
        return !config.getBoolean(path);
    }

    private void registerConfigDefaults() {
        config.registerDefault("defaultGameModeEnabled", true);
        config.registerDefault("defaultGameMode", "ADVENTURE");
        config.registerDefault("cancelDamage", true);
        config.registerDefault("cancelBlockEvents", true);
        config.registerDefault("disableJoinAndQuitMessages", true);
        config.registerDefault("tagsEnabled", true);
    }

    public FilePluginConfig getConfig() {
        return config;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
