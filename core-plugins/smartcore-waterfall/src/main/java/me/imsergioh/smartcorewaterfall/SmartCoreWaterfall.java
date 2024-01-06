package me.imsergioh.smartcorewaterfall;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.smartcorewaterfall.command.*;
import me.imsergioh.smartcorewaterfall.command.friend.FriendCommand;
import me.imsergioh.smartcorewaterfall.customcommand.MessageCommand;
import me.imsergioh.smartcorewaterfall.customcommand.TestCommand;
import me.imsergioh.smartcorewaterfall.instance.BungeeLogger;
import me.imsergioh.smartcorewaterfall.listener.*;
import me.imsergioh.smartcorewaterfall.manager.CustomCommandsManager;
import me.imsergioh.smartcorewaterfall.manager.OfflinePlayerDataManager;
import me.imsergioh.smartcorewaterfall.manager.TabHandler;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event.FriendEventManagement;
import me.imsergioh.smartcorewaterfall.messages.FriendManagerMessages;
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
        new FriendManagerMessages();
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
                new FriendAndPartyListeners(),
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
                //new FriendCommand(),
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
        FriendEventManagement.registerEvents();
    }

    private void registerDefaultLanguages() {
        for (Language languages : Language.values()) {
            LanguagesHandler.register(languages);
        }
    }

    public Logger getLogger() {
        return super.getLogger();
    }

}
