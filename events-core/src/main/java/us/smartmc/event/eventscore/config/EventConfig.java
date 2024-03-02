package us.smartmc.event.eventscore.config;

import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

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

    public <T extends Enum<?>> void setEnumType(String path, T type) {
        set(path, type.name());
    }

    public <T extends Enum<T>> T getEnumType(String path, Class<T> tClass) {
        return Enum.valueOf(tClass, getString(path));
    }

    public List<String> getParticipants() {
        return getStringList(PARTICIPANTS_KEY);
    }

    public String getHoster() {
        return getString(HOSTER_KEY);
    }
}
