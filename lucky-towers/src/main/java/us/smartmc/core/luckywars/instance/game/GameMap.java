package us.smartmc.core.luckywars.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Location;
import us.smartmc.core.luckywars.LuckyWars;
import us.smartmc.core.util.ConfigUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GameMap {

    private static final LuckyWars plugin = LuckyWars.getPlugin();
    private static final File MAPS_DIRECTORY = new File(plugin.getDataFolder() + "/maps");

    private static final String SPAWN_PATH = "spawn";
    private static final String POS1_PATH = "pos1";
    private static final String POS2_PATH = "pos2";
    private static final String LOCATIONS_PATH = "spawnLocations";

    private final String name;
    private final FilePluginConfig config;

    private GameTemplate template;

    private final List<Location> spawnLocations = new ArrayList<>();

    public GameMap(String name) {
        this.name = name;
        this.config = getConfig(name).load();
        registerConfigDefaults();
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

    public Location getPos1() {
        return getConfigLocation(POS1_PATH);
    }

    public Location getPos2() {
        return getConfigLocation(POS2_PATH);
    }

    public Location getSpawn() {
        return getConfigLocation(SPAWN_PATH);
    }

    private Location getConfigLocation(String path) {
        return ConfigUtils.documentToLocation(config.get(path, Document.class));
    }

    private void setConfigLocation(String path, Location location, boolean withYawPitch) {
        config.put(path, ConfigUtils.locationToDocument(location, withYawPitch));
        config.save();
    }

    private static FilePluginConfig getConfig(String name) {
        return new FilePluginConfig(new File(MAPS_DIRECTORY, name + ".json"));
    }

}
