package us.smartmc.event.eventscore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.event.eventscore.config.EventConfig;
import us.smartmc.event.eventscore.config.LanguageConfig;

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
    }

    @Override
    public void onDisable() {

    }
}
