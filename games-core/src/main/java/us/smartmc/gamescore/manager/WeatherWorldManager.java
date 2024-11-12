
package us.smartmc.gamescore.manager;

import lombok.Getter;
import org.bukkit.WeatherType;
import org.bukkit.World;

import java.util.*;

@Getter
public class WeatherWorldManager {

    private static final Set<String> worlds = new HashSet<>();

    private final World world;
    private final WeatherType type;

    public WeatherWorldManager(World world, WeatherType type) {
        this.world = world;
        this.type = type;
        int weatherDuration = switch (type) {
            case CLEAR -> 0;
            case DOWNFALL -> 7440;
        };
        world.setWeatherDuration(weatherDuration);
        worlds.add(world.getName());
    }

    public static boolean isActive(World world) {
        return worlds.contains(world.getName());
    }

    public static void remove(World world) {
        worlds.remove(world.getName());
    }

}
