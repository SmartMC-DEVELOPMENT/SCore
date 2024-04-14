package us.smartmc.core.instance;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;

import java.io.File;
import java.util.*;

public class PluginScoreboard {

    @Getter
    private final String name;
    private final FilePluginConfig config;

    private final Map<Integer, String> variablesLines = new HashMap<>();

    public PluginScoreboard(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(SmartCore.getPlugin().getDataFolder() + "/scoreboards", name + ".json"));
        config.load();
        config.registerDefault("title", "TITLE");
        config.registerDefault("scores", Arrays.asList("Line1", "Line2", "Line3", "play.smartmc.us"));
        config.registerDefault("update_ticks", 60);
        config.save();

        registerVariables();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                update(player);
            }
        }, 10, config.getInteger("update_ticks"));
    }

    private void registerVariables() {
        List<String> scores = getScores();
        for (int index = 0; index < scores.size(); index++) {
            String line = scores.get(index);
            if (line.contains("<") || line.contains("%")) {
                variablesLines.put(index, line);
                System.out.println("scoreline " + index + "= " + line);
            }
        }
    }

    public void register(Player player) {
        Bukkit.getScheduler().runTask(SmartCore.getPlugin(), () -> {
            createScoreboard(player);
        });
    }

    private void update(Player player) {
        BPlayerBoard board = getOrCreateBoard(player);
        for (Map.Entry<Integer, String> entry : variablesLines.entrySet()) {
            int index = entry.getKey();
            String line = entry.getValue();
            int reversedIndex = getScores().size() - index;
            board.update(line, reversedIndex);
        }
    }

    private BPlayerBoard getOrCreateBoard(Player player) {
        BPlayerBoard board = BPlayerBoard.get(player);
        if (board == null) {
            board = BPlayerBoard.create(player, getTitle());
        }
        return board;
    }

    private void createScoreboard(Player player) {
        BPlayerBoard board = getOrCreateBoard(player);
        List<String> scores = new ArrayList<>(getScores());


        for (int index = scores.size() - 1; index >= 0; index--) {
            String line = scores.get(index);
            board.set(line, scores.size() - index);
        }
    }

    public String getTitle() {
        return config.getString("title");
    }

    public List<String> getScores() {
        return config.getList("scores", String.class);
    }
}
