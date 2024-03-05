package us.smartmc.snowgames;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import us.smartmc.gamesmanager.gamesmanagerspigot.GamesManagerAPI;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GamePreset;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePresetManager;
import us.smartmc.snowgames.actions.GameActions;
import us.smartmc.snowgames.actions.HotbarActions;
import us.smartmc.snowgames.config.DefaultConfig;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.listener.BlockListeners;
import us.smartmc.snowgames.listener.GameListeners;
import us.smartmc.snowgames.listener.PlayerListeners;
import us.smartmc.snowgames.listener.SnowBallDamageListener;
import us.smartmc.snowgames.manager.*;
import us.smartmc.snowgames.messages.PluginMessages;
import us.smartmc.snowgames.player.FFAPlayer;
import us.smartmc.snowgames.util.DebugUtil;
import us.smartmc.snowgames.variables.PlayerVariables;

import java.io.File;

public class FFAPlugin extends GamesManagerAPI<FFAGame, FFAPlayer> {

    private static FFAPlugin ffaPlugin;

    @Getter
    private static FFAGame game;

    @Getter
    private LanguageConfig languageConfig;

    @Getter
    private DefaultConfig defaultConfig;

    private FFAPlayerManager<FFAPlayer> playerManager;
    @Getter
    private GameManager<FFAGame> gameManager;
    @Getter
    private ArenaManager arenaManager;

    @Getter
    private TopsManager topsManager;

    @Override
    public void onEnable() {
        ffaPlugin = this;
        getDataFolder().mkdirs();

        setupConfig();
        languageConfig = new LanguageConfig();

        new PluginMessages();

        playerManager = new FFAPlayerManager<>(this);
        gameManager = new GameManager<>();

        new GamePresetManager(getDataFolder() + "/games");
        new WorldConfigManager();
        GamePreset ffaPreset = new GamePreset("ffa", new SpigotYmlConfig(new File(getDataFolder() + "/game_preset.yml")));
        game = new FFAGame(gameManager, ffaPreset);

        registerListeners(
                PlayerListeners.class,
                GameListeners.class,
                BlockListeners.class,
                SnowBallDamageListener.class);

        registerVariables();

        ItemActionsManager.registerCommand("game", new GameActions());
        ItemActionsManager.registerCommand("hotbar", new HotbarActions());

        getCommand("ffa").setExecutor(new FFACommand());

        arenaManager = new ArenaManager();
        topsManager = new TopsManager("player_data", "snowgames_ffa")
                .register("kills", "deaths", "max_kill_streak");

        SyncUtil.later(() -> {
            topsManager.load();
        }, 750);

        DebugUtil.setEnabled(false);
    }

    @Override
    public void onDisable() {
        // ON DISABLE GAME LOGIC HERE
        for (World world : Bukkit.getWorlds()) {
            BlocksResetManager.completeAllByWorldName(world.getName());
        }
        playerManager.unregisterAll();
    }

    @SafeVarargs
    private final void registerListeners(Class<? extends Listener>... classes) {
        for (Class<? extends Listener> listenerClass : classes) {
            try {
                Bukkit.getPluginManager().registerEvents(listenerClass.newInstance(), ffaPlugin);
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
        return ffaPlugin.getConfig();
    }

    private void rotateMapTask() {
        arenaManager.rotateMap();
    }

    @Override
    public GamePlayerManager<FFAPlayer> getGamePlayerManager() {
        return playerManager;
    }

    public static FFAPlugin getFFAPlugin() {
        return ffaPlugin;
    }
}
