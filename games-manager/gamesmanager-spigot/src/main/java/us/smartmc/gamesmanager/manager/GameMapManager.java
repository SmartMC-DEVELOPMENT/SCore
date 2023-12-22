package us.smartmc.gamesmanager.manager;

import org.bukkit.World;
import us.smartmc.gamesmanager.GamesManagerSpigot;
import us.smartmc.gamesmanager.game.map.GameMap;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class GameMapManager {

    private static final GamesManagerSpigot plugin = GamesManagerSpigot.getPlugin();

    private static final HashMap<String, GameMap> maps = new HashMap<>();

    private static File mapsDirectory;

    public static void setupDefaults() {
        mapsDirectory = new File(plugin.getDataFolder() + "/maps");
        mapsDirectory.mkdirs();

        for (File configFile : Objects.requireNonNull(mapsDirectory.listFiles())) {
            load(configFile.getName().replace(".json", ""));
        }
    }

    private static void load(String name) {
        maps.put(name, new GameMap(name));
    }

    public static void register(String name, World world) {
        maps.put(world.getName(), new GameMap(name, world));
    }

    public static void unregister(String name) {
        maps.remove(name);
    }

    public static GameMap get(String name) {
        return maps.get(name);
    }

    public static File getMapsDir() {
        return mapsDirectory;
    }
}
