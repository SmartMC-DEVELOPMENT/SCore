package us.smartmc.core;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.instance.exceptionlistener.SendExceptionToDiscordListener;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import me.imsergioh.pluginsapi.util.GlobalExceptionHandler;
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
import us.smartmc.core.listener.SanctionListeners;
import us.smartmc.core.messages.GeneralMessages;
import us.smartmc.core.messages.ItemUtilsMessages;
import us.smartmc.core.util.ServerUtils;
import us.smartmc.core.variables.*;

import java.io.File;
import java.util.Objects;

public class SmartCore extends JavaPlugin {

    public static final int RELEASE_NUM = 2403;
    public static final String RELEASE_ID = "release-" + RELEASE_NUM;

    public static final SpigotLogger logger = new SpigotLogger();
    public static final SendExceptionToDiscordListener discordExceptionListener = new SendExceptionToDiscordListener()
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
                serverName = ServerUtils.readBackendProperty("server-name");
                if (serverName == null) {
                    serverName = "bukkit-server:" + Bukkit.getPort();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serverName;
    }

    public static String getServerID() {
        if (serverID == null) {
            try {
                serverID = ServerUtils.readBackendProperty("server-id");
                if (serverID == null) {
                    serverID = "bukkit-server:" + Bukkit.getPort();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        RedisConnection.mainConnection = new RedisConnection(config.getString("redis_host"),
                config.get("redis_port", Number.class).intValue());
        PubSubConnectionHandler.register(new ServerConnectionsHandler());

        SpigotPluginsAPI.setup(plugin);

        registerDefaultLanguages();

        SpawnHandler.setup();

        if (config.getBoolean("scoreboards_enabled"))
            scoreboardHandler = new ScoreboardHandler();
        if (config.getBoolean("lobbyhandler_enabled"))
            lobbyHandler = new LobbyHandler(this);

        adminModeHandler = new AdminModeHandler();

        registerListeners();
        registerCommands();
        registerVariables();

        new GeneralMessages();
        new ItemUtilsMessages();

        serverNumber = Integer.parseInt(SmartCore.getServerName().replaceAll("[^0-9]", ""));

        SyncUtil.sync(() -> {
            logger.info("Plugin enabled successfully!");
        });
        GlobalExceptionHandler.registerListener(discordExceptionListener);


    }

    @Override
    public void onDisable() {
        // Delete all redis cache with serverID (pattern: *.serverID)
        for (String key : RedisConnection.mainConnection.getResource().keys("*." + getServerName())) {
            RedisConnection.mainConnection.getResource().del(key);
        }
    }

    private void registerCommands() {
        plugin
                .regCMD("setspawn", new SetSpawnCommand())
                .regCMD("spawn", new SpawnCommand())
                .regCMD("admin", new AdminCommand())
                .regCMD("reloadLanguageConfig", new LanguageHandleConfigs())
                .regCMD("gamemode", new GameModeCommand())
                .regCMD("executeAtBungeeCommand", new ExecuteAtBungeeCommand())
                .regCMD("coins", new CoinsCommand())
                .regCMD("enigmaboxes", new EnigmaBoxesCommand())
                .regCMD("gems", new GemsCommand());

        ItemActionsManager.registerCommand("bungeeCMD", new BungeeCommandAction());
        ItemActionsManager.registerCommand("message", new MessageCommand());
    }

    private void registerListeners() {
        registerListeners(
                new SpawnHandler(),
                new TagsHandler(),
                new AdminModeListeners(),
                new CorePlayersListener(),
                new CommandListeners(),
                new SanctionListeners());
        if (scoreboardHandler != null) registerListeners(scoreboardHandler);
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

        VariablesHandler.register(new HexVariables());
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
                .registerDefault("scoreboards_enabled", true)
                .registerDefault("lobbyhandler_enabled", true)
                .save();
    }

    @SuppressWarnings("unused")
    private void registerServerName() {
        RedisConnection.mainConnection.getResource().set("serverAlias." + getServerName(), getServerAlias());
    }
}
