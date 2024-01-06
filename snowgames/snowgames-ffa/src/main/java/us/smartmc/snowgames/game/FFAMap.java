package us.smartmc.snowgames.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.manager.GameMapManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.util.LocationUtils;

@Getter
public class FFAMap extends GameMap {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    @Setter
    private int timeAlive = getMaxArenaTime();
    private BukkitTask timeTask;

    public FFAMap(String name) {
        super(name);
        registerConfigDefault("spawn_location", "world 0 0 0 0 0");
        registerConfigDefault("death_y_loc", 20);
        GameMapManager.register(this);
        startChangeMapTask();
    }

    public void startChangeMapTask() {
        timeTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeAlive == 0) {
                    plugin.getArenaManager().rotateMap();
                    cancel();
                    return;
                }
                timeAlive--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public Location getSpawn() {
        String loc = config.getString("spawn_location");
        return LocationUtils.stringToLocation(loc);
    }

    public void setSpawn(Location location) {
        config.put("spawn_location", LocationUtils.locationToString(location));
        config.save();
    }

    public int getDeathYLocation() {
        return config.getInteger("death_y_loc");
    }

}
