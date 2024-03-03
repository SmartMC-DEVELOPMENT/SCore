package me.imsergioh.smartcorewaterfall;

import com.mongodb.MongoClientURI;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.smartcorewaterfall.command.*;
import me.imsergioh.smartcorewaterfall.command.admin.BroadcastCommand;
import me.imsergioh.smartcorewaterfall.command.admin.ServerHandlerCommand;
import me.imsergioh.smartcorewaterfall.command.admin.SetPrefixCommand;
import me.imsergioh.smartcorewaterfall.command.info.DiscordCommand;
import me.imsergioh.smartcorewaterfall.command.info.HelpCommand;
import me.imsergioh.smartcorewaterfall.command.info.StoreCommand;
import me.imsergioh.smartcorewaterfall.command.info.TwitterCommand;
import me.imsergioh.smartcorewaterfall.command.moderation.BanCommand;
import me.imsergioh.smartcorewaterfall.command.moderation.KickCommand;
import me.imsergioh.smartcorewaterfall.command.moderation.MuteCommand;
import me.imsergioh.smartcorewaterfall.command.moderation.WarnCommand;
import me.imsergioh.smartcorewaterfall.customcommand.MessageCommand;
import me.imsergioh.smartcorewaterfall.customcommand.TestCommand;
import me.imsergioh.smartcorewaterfall.instance.BungeeLogger;
import me.imsergioh.smartcorewaterfall.listener.*;
import me.imsergioh.smartcorewaterfall.manager.CustomCommandsManager;
import me.imsergioh.smartcorewaterfall.manager.OfflinePlayerDataManager;
import me.imsergioh.smartcorewaterfall.manager.OnlineCountHandler;
import me.imsergioh.smartcorewaterfall.manager.TabHandler;
import me.imsergioh.smartcorewaterfall.messages.HelpMessages;
import me.imsergioh.smartcorewaterfall.messages.ProxyMainMessages;
import me.imsergioh.smartcorewaterfall.messages.SanctionsManagerMessages;
import me.imsergioh.smartcorewaterfall.rediscommand.LanguageChangeCommand;
import me.imsergioh.smartcorewaterfall.rediscommand.PlayerChatCommand;
import me.imsergioh.smartcorewaterfall.rediscommand.StopServerCommand;
import me.imsergioh.smartcorewaterfall.variables.LanguageVariables;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bson.Document;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

public final class SmartCoreWaterfall extends Plugin {

    @Getter
    private static SmartCoreWaterfall plugin;

    public static final BungeeLogger logger = new BungeeLogger();

    @Getter
    private static FilePluginConfig config;

    private static CustomCommandsManager mainCommandsManager;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

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

        logger.info("Plugin enabled successfully!");


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
                new RankListeners(),
                new BungeeMessagingListeners());
    }

    private void registerCommands() {
        registerCommands(
                new SmartCoreWaterfallCommand("bsmartcore"),
                new SetPrefixCommand(),
                new LobbyCommand(),
                new BanCommand(),
                new KickCommand(),
                new WarnCommand(),
                new MuteCommand(),
                new HelpCommand(),
                new BroadcastCommand(),
                new DiscordCommand(),
                new TwitterCommand(),
                new StoreCommand(),
                new SmartCoreWaterfallCommand("smartcorewaterfall"),
                new ServerHandlerCommand());
    }

    private void registerCommands(Command... commands) {
        for (Command command : commands) {
            getProxy().getPluginManager().registerCommand(plugin, command);
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getProxy().getPluginManager().registerListener(this, listener);
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

    public Logger getLogger() {
        return super.getLogger();
    }

}
