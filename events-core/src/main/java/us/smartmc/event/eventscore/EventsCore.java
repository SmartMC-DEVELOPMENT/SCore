package us.smartmc.event.eventscore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.event.eventscore.config.EventConfig;

import java.io.File;

public final class EventsCore extends JavaPlugin {

    @Getter
    private static EventsCore core;

    @Getter
    private EventConfig eventConfig;

    @Override
    public void onEnable() {
        core = this;
        eventConfig = new EventConfig(core);
    }

    @Override
    public void onDisable() {

    }


}
