package us.smartmc.event.eventscore.config;

import com.sun.tools.javac.util.List;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EventConfig extends SpigotYmlConfig {

    private static final String HOSTER_KEY = "hosteador";
    private static final String PARTICIPANTS_KEY = "participantes";

    public EventConfig(JavaPlugin plugin) {
        super(new File(plugin.getDataFolder(), "event_config.yml"));
        registerDefaults();
    }

    private void registerDefaults() {
        register(HOSTER_KEY, "HosteadorPro_YTHD");
        register(PARTICIPANTS_KEY, List.of("ImSergioh", "iNxlled", "Urugodd"));

        save();
    }

    public String getHoster() {
        return getString(HOSTER_KEY);
    }
}
