package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import us.smartmc.core.util.ConfigUtils;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.manager.GameMapManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Getter
public class GameMap {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    private static final String CENTER_PATH = "center";
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
            list.add(ConfigUtils.locationToDocument(location, false, true));
        }
        config.put(LOCATIONS_PATH, list);
        config.save();
    }

    public List<Location> loadConfigLocations(World world) {
        List<Location> list = new ArrayList<>();
        if (config.containsKey(LOCATIONS_PATH))
            config.getList(LOCATIONS_PATH, Document.class).forEach(doc -> list.add(ConfigUtils.documentToLocation(world, doc)));
        return list;
    }

    public Material getIcon() {
        return Material.getMaterial(config.getString("icon_material"));
    }

    private void registerConfigDefaults() {
        template = GameTemplate.get(config.get("template", "default"));

        // Loads the spawnLocations from config
        spawnLocations.addAll(loadConfigLocations(Bukkit.getWorld("maps")));

        config.registerDefault("icon_material", Material.GRASS.name());

        if (config.containsKey(MAINTENANCE_PATH))
            maintenance = config.getBoolean(MAINTENANCE_PATH);

        config.save();
    }


    public void setPos1(Location location) throws Exception {

        Location center = getConfigCenter(location.getWorld());
        if (center == null) throw new Exception("No spawn has been set already");

        double deltaX = location.getX() - center.getX();
        double deltaZ = location.getZ() - center.getZ();

        Location newLocation = new Location(
                location.getWorld(),
                deltaX,
                255,
                deltaZ
        );

        setConfigLocation(POS1_PATH, newLocation, false, false);
    }

    public void setPos2(Location location) throws Exception {
        Location center = getConfigCenter(location.getWorld());
        if (center == null) throw new Exception("No spawn has been set already");

        double deltaX = location.getX() - center.getX();
        double deltaZ = location.getZ() - center.getZ();

        Location newLocation = new Location(
                location.getWorld(),
                deltaX,
                -64,
                deltaZ
        );
        setConfigLocation(POS2_PATH, newLocation, false, false);
    }

    public void addSpawnLocation(Location location) throws Exception {
        Location center = getConfigCenter(location.getWorld());
        if (center == null) throw new Exception("No spawn has been set already");

        double deltaX = location.getX() - center.getX();
        double deltaY = location.getY() - center.getY();
        double deltaZ = location.getZ() - center.getZ();

        Location newLocation = new Location(
                location.getWorld(),
                deltaX,
                deltaY,
                deltaZ
        );
        spawnLocations.add(newLocation);
        saveSpawnLocations();
    }

    public void removeLastSpawn() throws Exception {
        if (spawnLocations.isEmpty()) throw new Exception("No more spawns available!");
        spawnLocations.remove(spawnLocations.size() - 1);
        saveSpawnLocations();
    }

    public void setCenter(Location location) {
        setConfigLocation(CENTER_PATH, location, false, true);
    }

    public void setTimeLimit(int time) {
        getConfig().put(TIME_LIMIT_PATH, time);
    }

    public Location getPos1(World world, int xAddition) {
        Location center = getConfigCenter(world);
        Location location = getConfigLocation(world, POS1_PATH).clone();
        return location.add(xAddition + center.getX(), center.getY(), center.getZ());
    }

    public Location getPos2(World world, int xAddition){
        Location center = getConfigCenter(world);
        Location location = getConfigLocation(world, POS2_PATH).clone();
        return location.add(xAddition + center.getX(), center.getY(), center.getZ());
    }

    public Location getSpawn(World world, int xLocationAddition) {
        return new Location(world, xLocationAddition, 70, 0);
    }

    public Location getConfigCenter(World world) {
        return getConfigLocation(world, CENTER_PATH).clone();
    }

    public int getTimeLimit() {
        return config.getInteger(TIME_LIMIT_PATH, 480);
    }

    private Location getConfigLocation(World world, String path) {
        return ConfigUtils.documentToLocation(world, config.get(path, Document.class));
    }

    private void setConfigLocation(String path, Location location, boolean includeWorld, boolean includeYawAndPitch) {
        config.put(path, ConfigUtils.locationToDocument(location, includeWorld, includeYawAndPitch));
        config.save();
    }

    private static FilePluginConfig getConfig(String name) {
        return new FilePluginConfig(new File(GameMapManager.MAPS_CONFIG_DIRECTORY, name + ".json"));
    }

}
