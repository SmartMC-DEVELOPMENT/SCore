package us.smartmc.event.eventscore;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.event.eventscore.command.OptionsCommand;
import us.smartmc.event.eventscore.config.EventConfig;
import us.smartmc.event.eventscore.config.LanguageConfig;
import us.smartmc.event.eventscore.itemcommand.OpenEventControlCommand;
import us.smartmc.event.eventscore.listener.CorePlayerListeners;
import us.smartmc.event.eventscore.listener.InventoryListeners;

import java.util.Objects;

public final class EventsCore extends JavaPlugin {

    @Getter
    private static EventsCore core;

    @Getter
    private EventConfig eventConfig;
    @Getter
    private LanguageConfig languageConfig;

    @Override
    public void onEnable() {
        core = this;
        MongoDBConnection.mainConnection = new MongoDBConnection("127.0.0.1", 27017);
        RedisConnection.mainConnection = new RedisConnection("127.0.0.1", 6379);
        SpigotPluginsAPI.setup(core);
        eventConfig = new EventConfig(core);
        languageConfig = new LanguageConfig();

        Objects.requireNonNull(getCommand("options")).setExecutor(new OptionsCommand());
        ItemActionsManager.registerCommand("openEventControl", new OpenEventControlCommand());

        registerListeners(new InventoryListeners(), new CorePlayerListeners());
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, core);
        }
    }
}
