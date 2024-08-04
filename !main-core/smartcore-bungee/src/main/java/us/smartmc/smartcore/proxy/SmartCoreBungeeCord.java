package us.smartmc.smartcore.proxy;

import com.mongodb.MongoClientURI;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bson.Document;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.smartcore.proxy.backend.service.PlayersService;
import us.smartmc.smartcore.proxy.command.CoreServerCommand;
import us.smartmc.smartcore.proxy.command.LobbyCommand;
import us.smartmc.smartcore.proxy.command.SmartCoreVelocityCommand;
import us.smartmc.smartcore.proxy.command.admin.BroadcastCommand;
import us.smartmc.smartcore.proxy.command.admin.ServerHandlerCommand;
import us.smartmc.smartcore.proxy.command.admin.SetPrefixCommand;
import us.smartmc.smartcore.proxy.command.moderation.BanCommand;
import us.smartmc.smartcore.proxy.command.moderation.KickCommand;
import us.smartmc.smartcore.proxy.command.moderation.MuteCommand;
import us.smartmc.smartcore.proxy.command.moderation.WarnCommand;
import us.smartmc.smartcore.proxy.command.onlinestore.OnlineStoreCommand;
import us.smartmc.smartcore.proxy.customcommand.CmdCommand;
import us.smartmc.smartcore.proxy.customcommand.MessageCommand;
import us.smartmc.smartcore.proxy.customcommand.TestCommand;
import us.smartmc.smartcore.proxy.instance.CoreCommand;
import us.smartmc.smartcore.proxy.instance.onlinestore.AnnouncePackagePurchase;
import us.smartmc.smartcore.proxy.instance.onlinestore.AnnouncePackageRenew;
import us.smartmc.smartcore.proxy.listener.*;
import us.smartmc.smartcore.proxy.manager.*;
import us.smartmc.smartcore.proxy.messages.HelpMessages;
import us.smartmc.smartcore.proxy.messages.ProxyMainMessages;
import us.smartmc.smartcore.proxy.messages.SanctionsManagerMessages;
import us.smartmc.smartcore.proxy.rediscommand.LanguageChangeCommand;
import us.smartmc.smartcore.proxy.rediscommand.PlayerChatCommand;
import us.smartmc.smartcore.proxy.rediscommand.StopServerCommand;
import us.smartmc.smartcore.proxy.variables.LanguageVariables;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SmartCoreBungeeCord extends Plugin {

    @Getter
    private static ProxyServer proxyServer;

    @Getter
    private static BackendClient backendClient;

    @Override
    public void onEnable() {
        plugin = this;
        proxyServer = getProxy();

        loadConfig();
        setupBackendConnections();
        registerDefaultLanguages();

        BungeeCordPluginsAPI.setup(this, proxyServer);

        startBackendConnection();

        loadCustomCommands();
        // Register commands
        CustomCommandsManager.register("test", new TestCommand());
        CustomCommandsManager.register("message", new MessageCommand());
        CustomCommandsManager.register("cmd", new CmdCommand());

        loadMessages();
        registerCommands();
        TabHandler.register();
        registerListeners();
        VariablesHandler.register(new LanguageVariables());

        // Handler to update every 3 seconds online count in redis and update online count
        OnlineCountHandler.startTask();

        HostnameRulesManager.load();

        try {
            tebexPackageManager = new TebexPackageManager(config.getString("tebex_secret_key"));
            tebexCommandsManager = new TebexCommandsManager(tebexPackageManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AllowedCommandsManager.loadAvailableServerIds();

        getLogger().info("Plugin enabled successfully!");

        PubSubConnectionHandler.register(new LoginMessageHandler());
    }

    @Getter
    private static Plugin velocityPlugin;

    @Getter
    private static SmartCoreBungeeCord plugin;

    @Getter
    private static FilePluginConfig config;

    @Getter
    private TebexPackageManager tebexPackageManager;
    @Getter
    private TebexCommandsManager tebexCommandsManager;

    private void startBackendConnection() {
        try {
            backendClient = new BackendClient("127.0.0.1", 7723);
            backendClient.login("velocity-proxy", "PROXYSVPERP4SSWORDRO0T2024SUFICIENTEMENTELARGAYS3GVR4");
            new Thread(backendClient).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ServicesManager.registerServices(true, new PlayersService());
    }

    @Override
    public void onDisable() {
        OnlineCountHandler.unregister();
    }

    public void loadConfig() {
        config = new FilePluginConfig(getDataFolder() + "/config.json").load();
        config.registerDefault("proxyID", "proxy-CA" + new Random().nextInt(9999));
        config.registerDefault("authServers", Arrays.asList("auth", "auth1", "auth2"));
        config.registerDefault("hubRules", new Document().append("sg-*", "sg-l*"));
        config.registerDefault("mongodb_url", "mongodb://imsergioh:Aa@66.70.181.34:27017/admin?readPreference=primary&replicaSet=ecommerce&directConnection=true");
        config.registerDefault("tebex_secret_key", UUID.randomUUID());
        config.registerDefault("serverRedirections.variaty-smartmc-us", List.of("serie-variaty"));
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
                new OfflinePlayerDataManager(),
                new CustomCommandsListeners(),
                new SanctionsListeners(),
                new LoginListeners(),
                new TabHandlerListeners(),
                new HostnameRedirectionsListeners(),
                new AllowedCommandsListeners());
        PubSubConnectionHandler.register(new LoginMessageHandler());
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
                new SmartCoreVelocityCommand("bsmartcore"),
                new ServerHandlerCommand(),
                new AnnouncePackagePurchase(),
                new AnnouncePackageRenew(),
                new OnlineStoreCommand(),
                new CoreServerCommand());
    }

    private void registerCommands(CoreCommand... commands) {
        for (CoreCommand command : commands) {
            getProxy().getPluginManager().registerCommand(plugin, command);
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            BungeeCordPluginsAPI.proxy.getPluginManager().registerListener(plugin, listener);
        }
    }

    private void setupBackendConnections() {
        MongoDBConnection.mainConnection = new MongoDBConnection(new MongoClientURI(config.getString("mongodb_url")));
        RedisConnection.mainConnection = new RedisConnection("localhost", 6379);

        PubSubConnectionHandler.register(
                new StopServerCommand(),
                new PlayerChatCommand(),
                new LanguageChangeCommand(),
                new CustomCommandsSubscriber());
    }

    private void registerDefaultLanguages() {
        for (Language languages : Language.values()) {
            LanguagesHandler.register(languages);
        }
    }

    public String getProxyID() {
        return config.getString("proxyID");
    }

}
