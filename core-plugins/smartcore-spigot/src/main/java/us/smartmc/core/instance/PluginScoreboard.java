package us.smartmc.core.instance;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import us.smartmc.core.SmartCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.pluginsapi.util.ChatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PluginScoreboard {

    private final String name;
    private final HashMap<Player, List<String>> playerLinesMap = new HashMap<>();
    private final FilePluginConfig config;

    public PluginScoreboard(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(SmartCore.getPlugin().getDataFolder() + "/scoreboards", name + ".json"));
        config.load();
        config.registerDefault("title", "TITLE");
        config.registerDefault("scores", Arrays.asList("Line1", "Line2", "Line3", "play.smartmc.us"));
        config.save();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            playerLinesMap.keySet().forEach(player -> {
                updateScoreboard(player, false);
            });
        }, 10, 20);
    }

    public void register(Player player) {
        playerLinesMap.put(player, new ArrayList<>());
        updateScoreboard(player, true);
    }

    private void updateScoreboard(Player player, boolean clear) {
        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            try {
                BPlayerBoard board = Netherboard.instance().getBoard(player) == null
                        ? Netherboard.instance().createBoard(player, ChatUtil.parse(player, getTitle())) :
                        Netherboard.instance().getBoard(player);
                List<String> currentLines = new ArrayList<>(getScores());
                currentLines.replaceAll(l -> ChatUtil.parse(player, l));
                List<String> lastKnownLines = playerLinesMap.getOrDefault(player, new ArrayList<>());

                if (!hasLinesChanged(currentLines, lastKnownLines)) return;

                if (clear) board.clear();

                int score = currentLines.size();
                for (String line : currentLines) {
                    board.set(line, score);
                    score--;
                }
                playerLinesMap.put(player, currentLines);
            } catch (Exception e) {
                unregister(player);
            }
        }, 0);
    }

    public void unregister(Player player) {
        playerLinesMap.remove(player);
    }

    private boolean hasLinesChanged(List<String> currentLines, List<String> lastKnownLines) {
        return !lastKnownLines.equals(currentLines);
    }

    public String getTitle() {
        return config.getString("title");
    }

    public List<String> getScores() {
        return config.getList("scores", String.class);
    }

    public String getName() {
        return name;
    }
}
