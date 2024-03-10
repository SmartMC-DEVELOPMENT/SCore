package us.smartmc.smartcore.smartcorevelocity;

import com.google.inject.Inject;
import com.mongodb.MongoClientURI;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.*;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.command.*;
import us.smartmc.smartcore.smartcorevelocity.command.admin.BroadcastCommand;
import us.smartmc.smartcore.smartcorevelocity.command.admin.ServerHandlerCommand;
import us.smartmc.smartcore.smartcorevelocity.command.admin.SetPrefixCommand;
import us.smartmc.smartcore.smartcorevelocity.command.moderation.BanCommand;
import us.smartmc.smartcore.smartcorevelocity.command.moderation.KickCommand;
import us.smartmc.smartcore.smartcorevelocity.command.moderation.MuteCommand;
import us.smartmc.smartcore.smartcorevelocity.command.moderation.WarnCommand;
import us.smartmc.smartcore.smartcorevelocity.command.onlinestore.*;
import us.smartmc.smartcore.smartcorevelocity.customcommand.*;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.instance.onlinestore.AnnouncePackagePurchase;
import us.smartmc.smartcore.smartcorevelocity.instance.onlinestore.AnnouncePackageRenew;
import us.smartmc.smartcore.smartcorevelocity.listener.*;
import us.smartmc.smartcore.smartcorevelocity.manager.*;
import us.smartmc.smartcore.smartcorevelocity.messages.*;
import us.smartmc.smartcore.smartcorevelocity.rediscommand.*;
import us.smartmc.smartcore.smartcorevelocity.variables.*;
import us.smartmc.smartcore.velocitycore.manager.VelocityPluginsAPI;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Plugin(
        id = "smartcore-velocity",
        name = "smartcore-velocity",
        version = "1.0-SNAPSHOT"
)
public class SmartCoreVelocity {

    @Inject
    private Logger logger;

    @Getter
    private final Path dataDirectory;

    @Inject
    public SmartCoreVelocity(@DataDirectory Path path, ProxyServer proxyServer) {
        VelocityPluginsAPI.proxy = proxyServer;
        this.dataDirectory = path;
        initDataFolder();
    }

    @Getter
    private static Plugin velocityPlugin;

    @Getter
    private static SmartCoreVelocity plugin;

    @Getter
    private static FilePluginConfig config;

    @Getter
    private TebexPackageManager tebexPackageManager;
    @Getter
    private TebexCommandsManager tebexCommandsManager;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;

        loadConfig();
        setupBackendConnections();
        registerDefaultLanguages();

        loadCustomCommands();
        // Register commands
        CustomCommandsManager.register("test", new TestCommand());
        CustomCommandsManager.register("message", new MessageCommand());

        loadMessages();
        registerCommands();
        TabHandler.register();
        registerListeners();
        VariablesHandler.register(new LanguageVariables());

        // Handler to update every 3 seconds online count in redis and update online count
        OnlineCountHandler.startTask();

        try {
            tebexPackageManager = new TebexPackageManager(config.getString("tebex_secret_key"));
            tebexCommandsManager = new TebexCommandsManager(tebexPackageManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logger.info("Plugin enabled successfully!");
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        OnlineCountHandler.unregister();
    }

    public void loadConfig() {
        config = new FilePluginConfig(dataDirectory + "/config.json").load();
        config.registerDefault("proxyID", "proxy-CA" + new Random().nextInt(9999));
        config.registerDefault("authServers", Arrays.asList("auth", "auth1", "auth2"));
        config.registerDefault("hubRules", new Document().append("sg-*", "sg-l*"));
        config.registerDefault("mongodb_url", "mongodb://imsergioh:Aa@66.70.181.34:27017/admin?readPreference=primary&replicaSet=ecommerce&directConnection=true");
        config.registerDefault("tebex_secret_key", UUID.randomUUID());
        config.save();
    }

    public void loadMessages() {
        new SanctionsManagerMessages();
        new HelpMessages();
        new ProxyMainMessages();
    }

    public void loadCustomCommands() {
        // Load collection
        CustomCommandsManager.load("proxy_data", "custom_proxy_commands");
    }

    private void registerListeners() {
        registerListeners(
                new TabHandlerListeners(),
                new OfflinePlayerDataManager(),
                new CustomCommandsListeners(),
                new SanctionsListeners(),
                new BungeeMessagingListeners());
    }

    private void registerCommands() {
        registerCommands(
                new SetPrefixCommand(),
                new LobbyCommand(),
                new BanCommand(),
                new KickCommand(),
                new WarnCommand(),
                new MuteCommand(),
                new BroadcastCommand(),
                new SmartCoreVelocityCommand("smartcorevelocity"),
                new ServerHandlerCommand(),
                new AnnouncePackagePurchase(),
                new AnnouncePackageRenew(),
                new OnlineStoreCommand());
    }

    private void registerCommands(CoreCommand... commands) {
        for (CoreCommand command : commands) {
            CommandManager commandManager = VelocityPluginsAPI.proxy.getCommandManager();
            CommandMeta commandMeta = commandManager.metaBuilder(command.getName())
                    .plugin(this)
                    .build();
            VelocityPluginsAPI.proxy.getCommandManager().register(commandMeta, command);
        }
    }

    private void registerListeners(Object... listeners) {

        for (Object listener : listeners) {
            VelocityPluginsAPI.proxy.getEventManager().register(plugin, listener);
        }
    }

    private void setupBackendConnections() {
        MongoDBConnection.mainConnection = new MongoDBConnection(new MongoClientURI(config.getString("mongodb_url")));
        RedisConnection.mainConnection = new RedisConnection("localhost", 6379);

        PubSubConnectionHandler.register(
                new LanguageChangeCommand(),
                new StopServerCommand(),
                new PlayerChatCommand());
    }

    private void registerDefaultLanguages() {
        for (Language languages : Language.values()) {
            LanguagesHandler.register(languages);
        }
    }

    public String getProxyID() {
        return config.getString("proxyID");
    }

    public java.util.logging.Logger getLogger() {
        return (java.util.logging.Logger) logger;
    }

    private void initDataFolder() {
        if (!java.nio.file.Files.exists(dataDirectory)) {
            try {
                java.nio.file.Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                // Manejar la excepción como creas conveniente. Por ejemplo, podrías loguear un error.
                e.printStackTrace();
            }
        }
    }

}
