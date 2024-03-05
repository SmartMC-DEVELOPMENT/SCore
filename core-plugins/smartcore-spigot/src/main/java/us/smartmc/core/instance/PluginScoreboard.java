package us.smartmc.core.instance;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.util.ChatUtil;
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
    private final Map<Player, Map<Integer, String>> lastKnownPlayerVariables = new HashMap<>();

    public PluginScoreboard(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(SmartCore.getPlugin().getDataFolder() + "/scoreboards", name + ".json"));
        config.load();
        config.registerDefault("title", "TITLE");
        config.registerDefault("scores", Arrays.asList("Line1", "Line2", "Line3", "play.smartmc.us"));
        config.save();

        registerVariables();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            for (Player player : lastKnownPlayerVariables.keySet()) {
                checkUpdates(player);
            }
        }, 10, 20 * 3);
    }

    private void registerVariables() {
        List<String> scores = getScores();
        for (int index = 0; index < scores.size(); index++) {
            String line = scores.get(index);
            if (line.contains("<") || line.contains("%")) {
                variablesLines.put(index, line);
            }
        }
    }

    public void register(Player player) {
        Bukkit.getScheduler().runTask(SmartCore.getPlugin(), () -> {
            createScoreboard(player);
        });
    }

    private void checkUpdates(Player player) {
        BPlayerBoard board = getOrCreateBoard(player);
        Map<Integer, String> lastKnowingLines = lastKnownPlayerVariables.get(player);

        for (Map.Entry<Integer, String> entry : variablesLines.entrySet()) {
            int index = entry.getKey();

            String variable = entry.getValue();
            String line = ChatUtil.parse(player, variable);

            int reversedIndex = getScores().size() - 1 - index;
            String lastKnownLine = lastKnowingLines.get(reversedIndex);

            if (!line.equals(lastKnownLine)) {
                board.set(line, reversedIndex);
                lastKnowingLines.put(reversedIndex, line);
            }
        }
    }

    private BPlayerBoard getOrCreateBoard(Player player) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if (board == null) {
            board = Netherboard.instance().createBoard(player, ChatUtil.parse(player, getTitle()));
        }
        return board;
    }

    private void createScoreboard(Player player) {
        BPlayerBoard board = getOrCreateBoard(player);
        List<String> scores = new ArrayList<>(getScores());
        board.clear();

        Map<Integer, String> lastKnownLines = new HashMap<>();
        for (int index = scores.size() - 1; index >= 0; index--) {
            String line = ChatUtil.parse(player, scores.get(index));
            board.set(line, scores.size() - 1 - index); // Ajustar el índice aquí
            if (variablesLines.containsKey(index)) {
                lastKnownLines.put(index, line);
            }
        }
        lastKnownPlayerVariables.put(player, lastKnownLines);
    }

    public void unregister(Player player) {
        lastKnownPlayerVariables.remove(player);
    }

    public String getTitle() {
        return config.getString("title");
    }

    public List<String> getScores() {
        return config.getList("scores", String.class);
    }
}
