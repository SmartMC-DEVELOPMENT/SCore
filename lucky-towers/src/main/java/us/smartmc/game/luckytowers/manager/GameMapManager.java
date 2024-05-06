package us.smartmc.game.luckytowers.manager;

import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;

import java.io.File;
import java.util.Objects;

public class GameMapManager extends ManagerRegistry<String, GameMap> {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();
    public static final File MAPS_DIRECTORY = new File(plugin.getDataFolder() + "/maps");

    @Override
    public void load() {
        // Load current maps configs
        for (File file : Objects.requireNonNull(MAPS_DIRECTORY.listFiles())) {
            if (!file.getName().endsWith(".json")) continue;
            String name = file.getName().replace(".json", "");
            register(name, new GameMap(name));
        }
    }

    @Override
    public void unload() {

    }
}
