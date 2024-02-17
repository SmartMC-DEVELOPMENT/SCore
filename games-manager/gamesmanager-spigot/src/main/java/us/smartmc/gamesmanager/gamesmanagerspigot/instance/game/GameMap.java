package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.gamesmanager.gamesmanagerspigot.util.ServerUtil;

import java.io.File;

public abstract class GameMap implements IGameMap {

    private static final String MAX_PLAYER_CONFIG_KEY = "max_players";
    private static final String MIN_PLAYER_CONFIG_KEY = "min_players";

    protected final String name;
    protected final FilePluginConfig config;

    public GameMap(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(ServerUtil.getWorldContainer() + "/game_maps_configs", name + ".json")).load();
        registerConfigDefaults();
        config.save();
    }

    public void setMaxPlayers(int size) {
        config.put(MAX_PLAYER_CONFIG_KEY, size);
        config.save();
    }

    public void setMinPlayers(int size) {
        config.put(MIN_PLAYER_CONFIG_KEY, size);
        config.save();
    }

    private void registerConfigDefaults() {
        registerConfigDefault(MAX_PLAYER_CONFIG_KEY, 12);
        registerConfigDefault(MIN_PLAYER_CONFIG_KEY, 6);
    }

    protected void registerConfigDefault(String path, Object value) {
        config.registerDefault(path, value);
    }

    @Override
    public int getMaxPlayerSize() {
        return config.getInteger(MAX_PLAYER_CONFIG_KEY);
    }

    @Override
    public int getMinPlayersSize() {
        return config.getInteger(MIN_PLAYER_CONFIG_KEY);
    }

    @Override
    public String getName() {
        return name;
    }
}
