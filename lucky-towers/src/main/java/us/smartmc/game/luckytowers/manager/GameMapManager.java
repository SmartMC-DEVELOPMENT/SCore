package us.smartmc.game.luckytowers.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.map.MapsGeneration;

import java.io.File;
import java.util.Objects;

public class GameMapManager extends ManagerRegistry<String, GameMap> {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();
    public static final File MAPS_CONFIG_DIRECTORY = new File(LuckyTowers.DATA_DIR + "/maps");
    public static final File MAPS_SCHEMS_DIRECTORY = new File(LuckyTowers.DATA_DIR + "/maps-schems");
    @Getter
    private static MapsGeneration mainMapsGeneration;

    @Override
    public void load() {
        mainMapsGeneration = new MapsGeneration();
        // Load current maps configs
        MAPS_CONFIG_DIRECTORY.mkdirs();
        MAPS_SCHEMS_DIRECTORY.mkdirs();
        for (File file : Objects.requireNonNull(MAPS_CONFIG_DIRECTORY.listFiles())) {
            if (!file.getName().endsWith(".json")) continue;
            String name = file.getName().replace(".json", "");
            register(name, new GameMap(name));
        }
    }

    @Override
    public void unload() {

    }

    public World getWorld() {
        String name = plugin.getMainConfig().getMapsWorldDirName();
        World world = Bukkit.getWorld(name);
        if (world == null) world = new WorldCreator(name).createWorld();
        return world;
    }

}
