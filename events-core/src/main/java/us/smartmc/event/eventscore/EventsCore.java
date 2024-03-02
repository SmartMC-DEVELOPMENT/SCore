package us.smartmc.event.eventscore;

import lombok.Getter;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.event.eventscore.command.OptionsCommand;
import us.smartmc.event.eventscore.config.EventConfig;
import us.smartmc.event.eventscore.config.LanguageConfig;
import us.smartmc.event.eventscore.itemcommand.OpenEventControlCommand;

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
        eventConfig = new EventConfig(core);
        languageConfig = new LanguageConfig();

        Objects.requireNonNull(getCommand("options")).setExecutor(new OptionsCommand());

        ItemActionsManager.registerCommand("openEventControl", new OpenEventControlCommand());
    }

    @Override
    public void onDisable() {

    }
}
