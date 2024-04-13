package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;

import java.util.HashSet;
import java.util.Set;

public class GamePreset implements IGamePreset {

    private final String name;

    private final SpigotYmlConfig config;

    public GamePreset(String name, SpigotYmlConfig config) {
        this.name = name;
        this.config = config;
    }

    @Override
    public Set<String> getValidMaps() {
        return new HashSet<>(config.getStringList("valid_maps"));
    }

    @Override
    public String getName() {
        return name;
    }
}
