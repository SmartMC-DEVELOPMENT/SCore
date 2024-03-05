package us.smartmc.event.eventscore.config;

import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import us.smartmc.event.eventscore.EventsCore;

import java.io.File;

public class LanguageConfig extends SpigotYmlConfig {

    private static final EventsCore core = EventsCore.getCore();

    public LanguageConfig() {
        super(new File(core.getDataFolder(), "messages.yml"));
    }

}
