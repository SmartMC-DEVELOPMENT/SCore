package us.smartmc.gamescore.instance.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import us.smartmc.gamescore.instance.PluginScoreboard;
import us.smartmc.gamescore.manager.ScoreboardsManager;

@Getter
public class PlayerScoreboard {

    private final Player player;
    private final String pluginScoreboardId;

    private final Scoreboard scoreboard;
    private final Objective objective;

    private final PlayerScoreboardUpdateTask task;

    public PlayerScoreboard(Player player, PluginScoreboard scoreboard) {
        this.player = player;
        this.pluginScoreboardId = scoreboard.getId();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective(pluginScoreboardId, "dummy");
        this.task = new PlayerScoreboardUpdateTask(this);
    }

    public PluginScoreboard getPluginScoreboard() {
        ScoreboardsManager manager = getManager();
        if (manager == null) return null;
        return manager.get(pluginScoreboardId);
    }

    private static ScoreboardsManager getManager() {
        return ScoreboardsManager.getManager(ScoreboardsManager.class);
    }

}
