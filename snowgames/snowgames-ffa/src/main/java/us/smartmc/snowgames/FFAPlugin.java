package us.smartmc.snowgames;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamesmanager.game.GamePreset;
import us.smartmc.gamesmanager.manager.GamePresetManager;
import us.smartmc.snowgames.actions.GameActions;
import us.smartmc.snowgames.actions.HotbarActions;
import us.smartmc.snowgames.config.DefaultConfig;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.listener.BlockListeners;
import us.smartmc.snowgames.listener.DamageListeners;
import us.smartmc.snowgames.listener.GameListeners;
import us.smartmc.snowgames.listener.PlayerListeners;
import us.smartmc.snowgames.manager.ArenaManager;
import us.smartmc.snowgames.messages.PluginMessages;
import us.smartmc.snowgames.variables.PlayerVariables;

import java.io.File;

public class FFAPlugin extends JavaPlugin {

    @Getter
    private static FFAPlugin plugin;

    @Getter
    private static FFAGame game;

    @Getter
    private LanguageConfig languageConfig;

    @Getter
    private DefaultConfig defaultConfig;

    @Getter
    private ArenaManager arenaManager;


    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

        setupConfig();
        languageConfig = new LanguageConfig();
        arenaManager = new ArenaManager();

        new PluginMessages();

        new GamePresetManager(new File(getDataFolder() + "/games"));
        GamePreset preset = new GamePreset("ffa");
        game = new FFAGame(preset);

        registerListeners(
                PlayerListeners.class,
                GameListeners.class,
                BlockListeners.class,
                DamageListeners.class);

        registerVariables();

        ItemActionsManager.registerCommand("game", new GameActions());
        ItemActionsManager.registerCommand("hotbar", new HotbarActions());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::rotateMapTask, 0L, game.getMap().getMaxArenaTime() - 600);
    }

    @Override
    public void onDisable() {
        // ON DISABLE GAME LOGIC HERE
    }

    private void registerListeners(Class<? extends Listener>... classes) {

        for (Class<? extends Listener> listenerClass : classes) {
            try {
                Bukkit.getPluginManager().registerEvents(listenerClass.newInstance(), plugin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void registerVariables() {
        VariablesHandler.register(new PlayerVariables());
    }

    private void setupConfig() {
        defaultConfig = new DefaultConfig();
        defaultConfig.save();
    }

    public static FileConfiguration config() {
        return plugin.getConfig();
    }

    private void rotateMapTask() {
        arenaManager.rotateMap();
    }
}
