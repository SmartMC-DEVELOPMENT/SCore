package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Location;
import us.smartmc.core.util.ConfigUtils;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.manager.GameMapManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GameMap {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    private static final String SPAWN_PATH = "spawn";
    private static final String POS1_PATH = "pos1";
    private static final String POS2_PATH = "pos2";
    private static final String LOCATIONS_PATH = "spawnLocations";
    private static final String MAINTENANCE_PATH = "maintenance";
    private static final String TIME_LIMIT_PATH = "timeLimit";

    private final String name;
    private final FilePluginConfig config;

    private boolean maintenance = true;
    private GameTemplate template;

    private final List<Location> spawnLocations = new ArrayList<>();

    public GameMap(String name) {
        this.name = name;
        this.config = getConfig(name).load();
        registerConfigDefaults();
    }

    public boolean toggleMaintenance() {
        maintenance = !maintenance;
        config.put(MAINTENANCE_PATH, maintenance);
        config.save();
        return maintenance;
    }

    public void saveSpawnLocations() {
        List<Document> list = new ArrayList<>();
        for (Location location : spawnLocations) {
            list.add(ConfigUtils.locationToDocument(location, true));
        }
        config.put(LOCATIONS_PATH, list);
        config.save();
    }

    public List<Location> loadConfigLocations() {
        List<Location> list = new ArrayList<>();
        if (config.containsKey(LOCATIONS_PATH))
            config.getList(LOCATIONS_PATH, Document.class).forEach(doc -> list.add(ConfigUtils.documentToLocation(doc)));
        return list;
    }

    private void registerConfigDefaults() {
        template = GameTemplate.get(config.get("template", "default"));

        // Loads the spawnLocations from config
        spawnLocations.addAll(loadConfigLocations());

        if (config.containsKey(MAINTENANCE_PATH))
            maintenance = config.getBoolean(MAINTENANCE_PATH);

        config.save();
    }

    public void setPos1(Location location) {
        setConfigLocation(POS1_PATH, location, false);
    }

    public void setPos2(Location location) {
        setConfigLocation(POS2_PATH, location, false);
    }

    public void setSpawn(Location location) {
        setConfigLocation(SPAWN_PATH, location, true);
    }

    public void setTimeLimit(int time) {
        getConfig().put(TIME_LIMIT_PATH, time);
    }

    public Location getPos1() {
        return getConfigLocation(POS1_PATH);
    }

    public Location getPos2() {
        return getConfigLocation(POS2_PATH);
    }

    public Location getSpawn() {
        return getConfigLocation(SPAWN_PATH);
    }

    public int getTimeLimit() {
        return config.getInteger(TIME_LIMIT_PATH, 480);
    }

    private Location getConfigLocation(String path) {
        return ConfigUtils.documentToLocation(config.get(path, Document.class));
    }

    private void setConfigLocation(String path, Location location, boolean withYawPitch) {
        config.put(path, ConfigUtils.locationToDocument(location, withYawPitch));
        config.save();
    }

    private static FilePluginConfig getConfig(String name) {
        return new FilePluginConfig(new File(GameMapManager.MAPS_CONFIG_DIRECTORY, name + ".json"));
    }

}
