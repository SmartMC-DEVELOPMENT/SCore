package us.smartmc.core.instance;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.ScoreboardHandler;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class PluginScoreboard {

    @Getter
    private final String name;
    private final FilePluginConfig config;

    private final Map<Integer, String> variablesLines = new HashMap<>();
    private final Set<UUID> players = new HashSet<>();

    public PluginScoreboard(String name) {
        this.name = name;
        this.config = new FilePluginConfig(new File(SmartCore.getPlugin().getDataFolder() + "/scoreboards", name + ".json"));
        config.load();
        config.registerDefault("title", "TITLE");
        config.registerDefault("scores", Arrays.asList("Line1", "Line2", "Line3", "play.smartmc.us"));
        config.save();

        registerVariables();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            for (UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) return;
                update(player);
            }
        }, 10L, 10L);
    }

    public void forEachPlayer(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().stream().filter(p -> players.contains(p.getUniqueId())).forEach(consumer);
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
            players.add(player.getUniqueId());
        });
    }

    public void unregister(Player player) {
        players.remove(player.getUniqueId());
    }

    public boolean isPlayerView(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void update(Player player) {
        BPlayerBoard board = getOrCreateBoard(player);
        for (Map.Entry<Integer, String> entry : variablesLines.entrySet()) {
            int index = entry.getKey();
            String line = entry.getValue();
            int reversedIndex = getScores().size() - index;
            board.set(ChatUtil.parse(player, line), reversedIndex);
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
        List<String> scores = getScores();

        for (int index = scores.size() - 1; index >= 0; index--) {
            String line = scores.get(index);
            board.set(ChatUtil.parse(player, line), scores.size() - index);
        }
    }

    public int getTickUpdateDelay() {
        return config.containsKey("updateDelay") ? config.getInteger("updateDelay") : ScoreboardHandler.UPDATE_TASK_DELAY;
    }

    public String getTitle() {
        return config.getString("title");
    }

    public List<String> getScores() {
        return new ArrayList<>(config.getList("scores", String.class));
    }
}
