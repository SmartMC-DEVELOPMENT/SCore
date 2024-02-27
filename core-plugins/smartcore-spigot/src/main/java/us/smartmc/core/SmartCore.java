package us.smartmc.core;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
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
import us.smartmc.core.itemcommands.MessageCommand;
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
    public static final ExceptionHandler exceptionHandler = new ExceptionHandler()
            .addField("server-id", getServerID())
            .addField("server-name", getServerName())
            .addField("server-port", String.valueOf(Bukkit.getPort()))
            .addField("online-players", String.valueOf(Bukkit.getOnlinePlayers().size()));

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

    private static String serverName;
    private static String serverID;
    @Getter
    private static int serverNumber;

    public static String getServerAlias() {
        return config.getString("alias");
    }

    public static String getServerName() {
        if (serverName == null) {
            try {
                serverName = ServerUtils.readServerProperty("server-name");
            } catch (Exception e) {
                serverName = "bukkit-server:" + Bukkit.getPort();
                System.out.println("Failed to read server property of server-name! Cached temporary to " + serverName);
            }
        }
        return serverName;
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
            PubSubConnectionHandler.register(new ServerConnectionsHandler());
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

        serverNumber = Integer.parseInt(SmartCore.getServerName().replaceAll("[^0-9]", ""));

        SyncUtil.sync(() -> {
            logger.info("Plugin enabled successfully!");
        });

        new Thread(() -> {
            while (true) {
                Thread.getAllStackTraces().keySet().forEach(thread -> {
                    if (thread.getUncaughtExceptionHandler() instanceof ExceptionHandler) return;
                    thread.setUncaughtExceptionHandler(exceptionHandler);
                });
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void onDisable() {
        // Delete all redis cache with serverID (pattern: *.serverID)
        for (String key : RedisConnection.mainConnection.getResource().keys("*." + getServerName())) {
            RedisConnection.mainConnection.getResource().del(key);
        }

        cuboidManager.unload();
    }

    private void registerCommands() {
        plugin
                .regCMD("setspawn", new SetSpawnCommand())
                .regCMD("spawn", new SpawnCommand())
                .regCMD("admin", new AdminCommand())
                .regCMD("reloadLanguageConfig", new LanguageHandleConfigs())
                .regCMD("gamemode", new GameModeCommand())
                .regCMD("region", new RegionCommand())
                .regCMD("executeAtBungeeCommand", new ExecuteAtBungeeCommand())
                .regCMD("friend", new FriendCommand())
                .regCMD("coins", new CoinsCommand());

        ItemActionsManager.registerCommand("bungeeCMD", new BungeeCommandAction());
        ItemActionsManager.registerCommand("message", new MessageCommand());
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
        // SERVER VARIABLES
        VariablesHandler.register(new ServerVariables());

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
        RedisConnection.mainConnection.getResource().set("serverAlias." + getServerName(), getServerAlias());
    }
}
