package us.smartmc.core.instance;

import com.google.common.collect.Lists;
import me.imsergioh.pluginsapi.util.BukkitChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class BPlayerBoard {

    private static final Map<UUID, BPlayerBoard> boards = new HashMap<>();
    private static final char[] validEntries = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private final Player player;

    private final Scoreboard scoreboard;
    private final Objective objective;

    public BPlayerBoard(Player player, String title) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("aaa", Criteria.DUMMY, "bbb");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.displayName(BukkitChatUtil.parse(player, title));
        player.setScoreboard(scoreboard);
    }

    public void clear() {
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
    }

    public void set(String line, int scoreIndex) {
        Team team = getOrCreateTeamByScoreIndex(scoreIndex);
        String entry = randomEmptyEntry(scoreIndex);
        team.addEntry(entry);
        team.prefix(BukkitChatUtil.parse(player, "&r" + line));
        objective.getScore(entry).setScore(scoreIndex);
    }

    public void update(String line, int scoreIndex) {
        Team team = getOrCreateTeamByScoreIndex(scoreIndex);
        team.prefix(BukkitChatUtil.parse(player, "&r" + line));
    }

    public void dealete() {
        boards.remove(player.getUniqueId());
    }

    private Team getOrCreateTeamByScoreIndex(int index) {
        String name = "l" + index;
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }
        return team;
    }

    private Collection<String> usedEntries(Scoreboard scoreboard) {
        Collection<String> output = Lists.newArrayList();
        // iterate all team names and add them to the output.
        for (Team current : scoreboard.getTeams()) {
            output.addAll(current.getEntries());
        }
        return output;
    }

    public String randomEmptyEntry(int index) {
        return "§" + validEntries[index];
    }

    public static BPlayerBoard get(Player player) {
        return boards.get(player.getUniqueId());
    }

    public static BPlayerBoard create(Player player, String title) {
        if (boards.containsKey(player.getUniqueId())) return boards.get(player.getUniqueId());
        BPlayerBoard board = new BPlayerBoard(player, title);
        boards.put(player.getUniqueId(), board);
        return board;
    }

    public void delete() {
        boards.remove(player.getUniqueId());
    }
}