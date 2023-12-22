package us.smartmc.gamesmanager.manager;

import lombok.Getter;
import us.smartmc.gamesmanager.game.GamePreset;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class GamePresetManager {

    private final HashMap<String, GamePreset> presets = new HashMap<>();

    @Getter
    private static File gamesDir;

    public GamePresetManager(File gamesDirectory) {
        gamesDir = gamesDirectory;
        gamesDir.mkdirs();
        loadGamePresetsFromGamesDir();
    }

    private void loadGamePresetsFromGamesDir() {
        for (File presetFile : Objects.requireNonNull(gamesDir.listFiles())) {
            String name = presetFile.getName().replace(".json", "");
            register(new GamePreset(name));
        }
    }

    public void register(GamePreset instance) {
        presets.put(instance.getName(), instance);
    }

    public void unregister(String name) {
        get(name).unload();
        presets.remove(name);
    }

    public GamePreset get(String name) {
        return presets.get(name);
    }

    public static File getGamesDirectory() {
        return gamesDir;
    }
}
