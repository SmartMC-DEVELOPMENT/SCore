package me.imsergioh.smartcorewaterfall;

import me.imsergioh.smartcorewaterfall.command.*;
import me.imsergioh.smartcorewaterfall.listener.RankListeners;
import me.imsergioh.smartcorewaterfall.manager.ServersHandler;
import me.imsergioh.smartcorewaterfall.manager.TabHandler;
import me.imsergioh.smartcorewaterfall.messages.HelpMessages;
import me.imsergioh.smartcorewaterfall.messages.ProxyMainMessages;
import me.imsergioh.smartcorewaterfall.rediscommand.PlayerChatCommand;
import org.bson.Document;
import us.smartmc.core.pluginsapi.connection.*;
import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.handler.PubSubConnectionHandler;
import us.smartmc.core.pluginsapi.handler.VariablesHandler;
import us.smartmc.core.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.pluginsapi.language.Language;
import me.imsergioh.smartcorewaterfall.customcommand.MessageCommand;
import me.imsergioh.smartcorewaterfall.customcommand.TestCommand;
import me.imsergioh.smartcorewaterfall.instance.BungeeLogger;
import me.imsergioh.smartcorewaterfall.listener.CustomCommandsListeners;
import me.imsergioh.smartcorewaterfall.listener.SanctionsListeners;
import me.imsergioh.smartcorewaterfall.listener.TabHandlerListeners;
import me.imsergioh.smartcorewaterfall.manager.CustomCommandsManager;
import me.imsergioh.smartcorewaterfall.manager.OfflinePlayerDataManager;
import me.imsergioh.smartcorewaterfall.messages.SanctionsManagerMessages;
import me.imsergioh.smartcorewaterfall.rediscommand.LanguageChangeCommand;
import me.imsergioh.smartcorewaterfall.rediscommand.StopServerCommand;
import me.imsergioh.smartcorewaterfall.variables.LanguageVariables;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;
import java.util.logging.Logger;

public final class SmartCoreWaterfall extends Plugin {

    private static SmartCoreWaterfall plugin;

    public static final BungeeLogger logger = new BungeeLogger();

    private static FilePluginConfig config;

    private static CustomCommandsManager mainCommandsManager;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();
        config = new FilePluginConfig(getDataFolder() + "/config.json").load();
        config.registerDefault("authServers", Arrays.asList("auth", "auth1", "auth2"));
        config.registerDefault("hubRules", new Document().append("sg-*", "sg-l*"));
        config.save();

        setupBackendConnections();
        registerDefaultLanguages();
        mainCommandsManager = new CustomCommandsManager("main");
        CustomCommandsManager.register("test", new TestCommand());
        CustomCommandsManager.register("message", new MessageCommand());

        new SanctionsManagerMessages();
        new HelpMessages();
        new ProxyMainMessages();

        registerCommands();

        TabHandler.register();
        registerListeners();

        for (Language language : Language.values()) {
            LanguagesHandler.register(language);
        }

        VariablesHandler.register(new LanguageVariables());

        logger.info("Plugin enabled successfully!");
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners() {
        registerListeners(
                new TabHandlerListeners(),
                new OfflinePlayerDataManager(),
                new CustomCommandsListeners(),
                new SanctionsListeners(),
                new RankListeners());
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
                new SmartCoreWaterfallCommand("smartcorewaterfall"));
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
        MongoDBConnection.mainConnection = new MongoDBConnection("localhost", 27017);
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

    public static FilePluginConfig getConfig() {
        return config;
    }

    public Logger getLogger() {
        return super.getLogger();
    }

    public static SmartCoreWaterfall getPlugin() {
        return plugin;
    }
}
