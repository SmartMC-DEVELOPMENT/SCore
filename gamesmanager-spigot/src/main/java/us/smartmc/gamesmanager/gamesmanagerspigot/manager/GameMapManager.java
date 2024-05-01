package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameMapManager {

    private static final Map<String, GameMap> maps = new HashMap<>();

    public static void register(GameMap map) {
        maps.put(map.getName(), map);
    }

    public static GameMap get(String name) {
        return maps.get(name);
    }

    public static Collection<GameMap> getValues() {
        return maps.values();
    }

}
