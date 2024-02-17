package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GamePreset;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GamePresetManager {

    private final Map<String, GamePreset> presets = new HashMap<>();

    private final File parentDirectory;

    public GamePresetManager(String dirPath) {
        parentDirectory = new File(dirPath);
    }

    public void register(GamePreset... presets) {

    }

}
