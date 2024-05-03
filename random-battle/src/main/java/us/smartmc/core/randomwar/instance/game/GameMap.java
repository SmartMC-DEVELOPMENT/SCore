package us.smartmc.core.randomwar.instance.game;

import lombok.Getter;
import us.smartmc.core.randomwar.RandomWar;

import java.io.File;

@Getter
public class GameMap {

    private static final RandomWar plugin = RandomWar.getPlugin();
    private static final File MAPS_DIRECTORY = new File(plugin.getDataFolder() + "/maps");

    private final String name;

    public GameMap(String name) {
        this.name = name;
    }
}
