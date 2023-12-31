
package us.smartmc.gamesmanager.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.manager.GamePresetManager;
import us.smartmc.gamesmanager.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GamePreset implements IGamePreset {

    protected final FilePluginConfig config;

    protected final String name;

    public GamePreset(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(GamePresetManager.getGamesDirectory(), name + ".json")).load();
        registerDefaults();
        config.save();
    }

    private void registerDefaults() {
        registerConfigDefault("in_development", true);
        registerConfigDefault("map_spawn", "world 0 0 0 0 0");
    }

    public void registerConfigDefault(String path, Object value) {
        if (config.containsKey(path)) return;
        config.put(path, value);
    }

    @Override
    public void unload() {
        LogUtils.log(getClass(), "Unloading preset " + name + "...");

        // TODO: HERE CODE THINGS FOR UNLOAD ->

        LogUtils.log(getClass(), "Unloaded preset " + name + "!");
    }

    @Override
    public List<String> getMapWhitelist() {
        return config.getList("maps_whitelist", String.class, new ArrayList<>());
    }

    @Override
    public void addMapToWhitelist(GameMap map) {
        List<String> list = getMapWhitelist();
        list.add(map.getName());
        config.put("maps_whitelist", list);
        config.save();
    }

    @Override
    public void removeMapFromWhitelist(GameMap map) {
        List<String> list = getMapWhitelist();
        list.remove(map.getName());
        config.put("maps_whitelist", list);
        config.save();
    }

    @Override
    public boolean isInDevelopment() {
        return config.getBoolean("in_development");
    }
}
