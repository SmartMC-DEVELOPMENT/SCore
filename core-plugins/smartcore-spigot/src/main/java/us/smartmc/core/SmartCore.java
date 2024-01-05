package us.smartmc.core;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.commands.*;
import us.smartmc.core.handler.*;
import us.smartmc.core.instance.SpigotLogger;
import us.smartmc.core.itemcommands.BungeeCommandAction;
import us.smartmc.core.listener.AdminModeListeners;
import us.smartmc.core.listener.CommandListeners;
import us.smartmc.core.listener.CorePlayersListener;
import us.smartmc.core.listener.RegionSetterListener;
import us.smartmc.core.messages.GeneralMessages;
import us.smartmc.core.messages.ItemUtilsMessages;
import us.smartmc.core.regions.CuboidManager;
import us.smartmc.core.regions.controller.RegionModeManager;
import us.smartmc.core.util.ServerUtils;
import us.smartmc.core.variables.*;

import java.io.File;
import java.util.Objects;

public class SmartCore extends JavaPlugin {

    public static final SpigotLogger logger = new SpigotLogger();
    @Getter
    private static SmartCore plugin;
    private static FilePluginConfig config;
    @Getter
    private ScoreboardHandler scoreboardHandler;
    @Getter
    private LobbyHandler lobbyHandler;
    @Getter
    private AdminModeHandler adminModeHandler;
    @Getter
    private RegionModeManager regionModeManager;
    @Getter
    private CuboidManager cuboidManager;

    private static String serverID;

    public static String getServerAlias() {
        return config.getString("alias");
    }

    public static String getServerID() {
        if (serverID == null) {
            try {
                serverID = ServerUtils.readServerProperty("server-id");
            } catch (Exception e) {
                serverID = "bukkit:" + Bukkit.getPort();
                System.out.println("Failed to read server property of server-id! Cached temporary to " + serverID);
            }
        }
        return serverID;
    }

    public static FilePluginConfig getDefaultPluginConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        registerDefaultConfig();

        MongoDBConnection.mainConnection = new MongoDBConnection(config.getString("mongodb_url"));

        new Thread(() -> {
            RedisConnection.mainConnection = new RedisConnection(config.getString("redis_host"),
                    config.get("redis_port", Number.class).intValue());
            SpigotPluginsAPI.setup(plugin);
        }).start();

        registerDefaultLanguages();

        SpawnHandler.setup();
        scoreboardHandler = new ScoreboardHandler();
        lobbyHandler = new LobbyHandler(this);
        adminModeHandler = new AdminModeHandler();
        regionModeManager = new RegionModeManager();
        cuboidManager = new CuboidManager();

        cuboidManager.load();

        registerListeners();
        registerCommands();
        registerVariables();

        new GeneralMessages();
        new ItemUtilsMessages();

        SyncUtil.sync(() -> {
            logger.info("Plugin enabled successfully!");
        });
    }

    @Override
    public void onDisable() {
        CountVariables.removeCacheCount();
        cuboidManager.unload();
    }

    private void registerCommands() {
        plugin
                .regCMD("setspawn", new SetSpawnCommand())
                .regCMD("spawn", new SpawnCommand())
                .regCMD("admin", new AdminCommand())
                .regCMD("reloadLanguageConfig", new LanguageHandleConfigs())
                .regCMD("gamemode", new GameModeCommand())
                .regCMD("region", new RegionCommand());

        ItemActionsManager.registerCommand("bungeeCMD", new BungeeCommandAction());
    }

    private void registerListeners() {
        registerListeners(scoreboardHandler,
                new SpawnHandler(),
                new TagsHandler(),
                new AdminModeListeners(),
                new CorePlayersListener(),
                new CommandListeners(),
                new RegionSetterListener());
    }

    private void registerVariables() {
        // MAIN PLAYER VARIABLES
        VariablesHandler.register(new PlayerMainVariables());

        // COUNT + LISTENER
        CountVariables countVariables = new CountVariables();
        VariablesHandler.register(countVariables);

        // LUCKPERMS
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            VariablesHandler.register(new LuckPermsVariables());
        }

        // LANGUAGE
        VariablesHandler.register(new LanguageVariables());

        // COUNTDOWNS
        VariablesHandler.register(new CountdownVariables());

        VariablesHandler.register(new DateVariables());
    }

    @SuppressWarnings("unused")
    private void checkWorldContainer(File dir) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().equals("level.dat")) {
                Bukkit.createWorld(new WorldCreator(dir.getName()));
            }
        }
    }

    private void registerDefaultLanguages() {
        for (Language languages : Language.values()) {
            LanguagesHandler.register(languages);
        }
    }

    private SmartCore regCMD(String name, CommandExecutor executor) {
        getCommand(name).setExecutor(executor);
        return plugin;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerDefaultConfig() {
        config = new FilePluginConfig(new File(plugin.getDataFolder(), "config.json"))
                .load()
                .registerDefault("alias", "default_server_name")
                .registerDefault("mongodb_url",
                        "mongodb://imsergioh:Aa676459938@66.70.181.34:27017/admin?readPreference=primary&replicaSet=ecommerce&directConnection=true")
                .registerDefault("redis_host", "66.70.181.34")
                .registerDefault("redis_port", 6379)
                .save();
    }

    @SuppressWarnings("unused")
    private void registerServerName() {
        RedisConnection.mainConnection.getResource().set("serverAlias." + getServerID(), getServerAlias());
    }

}
