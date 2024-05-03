package us.smartmc.core.randomwar.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bukkit.Location;
import us.smartmc.core.randomwar.RandomBattle;

import java.io.File;

@Getter
public class GameMap {

    private static final RandomBattle plugin = RandomBattle.getPlugin();
    private static final File MAPS_DIRECTORY = new File(plugin.getDataFolder() + "/maps");

    private final String name;
    private final FilePluginConfig config;

    private GameTemplate template;

    private Location pos1, pos2;

    public GameMap(String name) {
        this.name = name;
        this.config = getConfig(name).load();
        registerConfigDefaults();
    }

    public void registerConfigDefaults() {

    }

    private static FilePluginConfig getConfig(String name) {
        return new FilePluginConfig(new File(MAPS_DIRECTORY, name + ".json"));
    }

}
