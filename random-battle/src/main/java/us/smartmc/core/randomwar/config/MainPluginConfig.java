package us.smartmc.core.randomwar.config;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Location;
import us.smartmc.core.randomwar.RandomBattle;
import us.smartmc.core.util.ConfigUtils;

public class MainPluginConfig extends FilePluginConfig {

    private static final RandomBattle plugin = RandomBattle.getPlugin();

    private static final String LOBBY_ENABLED_PATH = "lobbyEnabled";
    private static final String LOBBY_LOCATION_PATH = "lobbyLocation";

    public MainPluginConfig() {
        super(plugin.getDataFolder() + "/config.json");
        registerDefault(LOBBY_ENABLED_PATH, true);
    }

    public void setLobby(Location location) {
        put(LOBBY_LOCATION_PATH, ConfigUtils.locationToDocument(location, true));
        save();
    }

    public static Location getLobby() {
        return ConfigUtils.documentToLocation(plugin.getMainConfig().get(LOBBY_LOCATION_PATH, Document.class));
    }

    public static boolean isLobbyEnabled() {
        return plugin.getMainConfig().getBoolean(LOBBY_ENABLED_PATH);
    }

}
