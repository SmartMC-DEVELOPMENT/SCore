package us.smartmc.game.luckytowers.config;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Location;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.core.util.ConfigUtils;

public class MainPluginConfig extends FilePluginConfig {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    private static final String LOBBY_ENABLED_PATH = "lobbyEnabled";
    private static final String LOBBY_LOCATION_PATH = "lobbyLocation";
    private static final String MAPS_WORLD_PATH = "mapsWorld";

    public MainPluginConfig() {
        super(plugin.getDataFolder() + "/config.json");
        load();
        registerDefault(LOBBY_ENABLED_PATH, true);
        registerDefault(MAPS_WORLD_PATH, "maps");
    }

    public String getMapsWorldDirName() {
        return getString(MAPS_WORLD_PATH);
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
