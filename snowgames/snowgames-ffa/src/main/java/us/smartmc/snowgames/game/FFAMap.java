package us.smartmc.snowgames.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.manager.GameMapManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.manager.BlocksResetManager;
import us.smartmc.snowgames.util.LocationUtils;
import us.smartmc.snowgames.util.WorldUtil;

@Getter
public class FFAMap extends GameMap {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    private static final String DISPLAY_NAME_PATH = "displayName";
    private static final String WORLD_NAME_PATH = "world";
    private static final String SPAWN_LOCATION_PATH = "spawnLocation";
    private static final String DEATH_Y_LOC_PATH = "death_y_loc";

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

}
