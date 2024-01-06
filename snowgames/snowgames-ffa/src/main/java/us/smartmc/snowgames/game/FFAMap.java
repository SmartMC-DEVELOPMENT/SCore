package us.smartmc.snowgames.game;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.util.LocationUtils;

public class FFAMap extends GameMap {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    @Getter
    private int aliveTime = getMaxArenaTime();

    public FFAMap(String name) {
        super(name);
        registerConfigDefault("spawn_location", "world 0 0 0 0 0");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            aliveTime--;
        }, 0, 20);
    }

    public Location getSpawn() {
        String loc = config.getString("spawn_location");
        return LocationUtils.stringToLocation(loc);
    }

    public void setSpawn(Location location) {
        config.put("spawn_location", LocationUtils.locationToString(location));
    }
}
