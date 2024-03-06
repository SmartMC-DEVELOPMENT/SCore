package us.smartmc.snowgames.game;

import lombok.Getter;
import org.bukkit.Location;
import us.smartmc.core.handler.SpawnHandler;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameMap;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameMapManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.util.LocationUtils;

@Getter
public class FFAMap extends GameMap {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();

    private static final String DISPLAY_NAME_PATH = "displayName";
    private static final String WORLD_NAME_PATH = "world";
    private static final String SPAWN_LOCATION_PATH = "spawnLocation";
    private static final String DEATH_Y_LOC_PATH = "death_y_loc";
    private static final String SPAWN_Y_LOC_PATH = "spawn_y_loc";

    private final FFAMap mapInstance;

    private long startTimestamp;
    private long endTimestamp;

    public FFAMap(String name) {
        super(name);
        mapInstance = this;
        registerConfigDefault(DISPLAY_NAME_PATH, name);
        registerConfigDefault(WORLD_NAME_PATH, name);
        registerConfigDefault(SPAWN_LOCATION_PATH, getWorldName() + " 0 75 0 0 0");
        registerConfigDefault(DEATH_Y_LOC_PATH, 0);
        config.save();
        GameMapManager.register(this);
    }

    public void setSpawnYLocation(int yLoc) {
        config.put(SPAWN_Y_LOC_PATH, yLoc);
    }

    public int getSpawnYLocation() {
        if (!config.containsKey(SPAWN_Y_LOC_PATH)) return SpawnHandler.getLocation().getBlockY() - 12;
        return config.getInteger(SPAWN_Y_LOC_PATH);
    }

    public void registerMapChange() {
        startTimestamp = System.currentTimeMillis() / 1000;
        endTimestamp = startTimestamp + getMaxArenaTime();
    }

    public Location getSpawn() {
        String loc = config.getString(SPAWN_LOCATION_PATH);
        return LocationUtils.stringToLocation(loc);
    }

    public void setSpawn(Location location) {
        config.put(SPAWN_LOCATION_PATH, LocationUtils.locationToString(location));
        config.save();
    }

    private String getWorldName() {
        return config.getString(WORLD_NAME_PATH);
    }

    public int getDeathYLocation() {
        return config.getInteger(DEATH_Y_LOC_PATH);
    }

    public String getDisplayName() {
        return config.getString(DISPLAY_NAME_PATH);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void reset() {

    }
}
