package us.smartmc.gamescore.instance.player;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.PluginScoreboard;

import java.util.*;

public class PlayerScoreboardUpdateTask extends BukkitRunnable {

    private static final JavaPlugin plugin = GamesCoreAPI.getApi().getPlugin();

    private final PlayerScoreboard playerScoreboard;
    private final PluginScoreboard pluginScoreboard;

    private int titleIndex = 0;
    private final Set<Integer> scoresToUpdate = new HashSet<>();
    private final List<String> entries;

    public PlayerScoreboardUpdateTask(PlayerScoreboard playerScoreboard) {
        this.playerScoreboard = playerScoreboard;
        this.pluginScoreboard = playerScoreboard.getPluginScoreboard();
        this.entries = generateEntriesBySize(pluginScoreboard.getLines().size());
        createScoreboard();
        runTaskTimer(plugin, 1L, pluginScoreboard.getTicksUpdateRate());
    }

    private void createScoreboard() {
        String unformattedTitle = pluginScoreboard.getTitleLines().get(titleIndex);
        Player player = playerScoreboard.getPlayer();
        String formattedTitle = ChatUtil.parse(player, unformattedTitle);
        playerScoreboard.getObjective().setDisplayName(formattedTitle);

        // Setup lines
        int lineIndex = pluginScoreboard.getLines().size() -1;
        for (String unformattedLine : pluginScoreboard.getLines()) {
            String formattedLine = ChatUtil.parse(player, unformattedLine);

            // If formatted and unformatted are not the same introduces index into a set for updating at run()
            if (!unformattedLine.equals(formattedLine)) {
                scoresToUpdate.add(lineIndex);
            }
            Team team = playerScoreboard.getScoreboard().registerNewTeam("l" + lineIndex);
            String entry = entries.get(lineIndex);
            team.addEntry(entry);
            if (formattedLine.length() > 16) {
                String suffix = formattedLine.substring(0, 16);
                String prefix = formattedLine.substring(16);
                team.setSuffix(suffix);
                team.setPrefix(prefix);
            }
            playerScoreboard.getObjective().getScore(entry).setScore(lineIndex);
            lineIndex--;
        }

        // Set scoreboard
        player.setScoreboard(playerScoreboard.getScoreboard());
        player.sendMessage("Lines=" + pluginScoreboard.getLines().toString());
    }

    @Override
    public void run() {
        // Title
        int oldTitleIndex = titleIndex;
        String nextTitle = getNextTitle();
        if (oldTitleIndex != titleIndex) {
            // Update title (Changed)
            String formattedTitle = ChatUtil.parse(playerScoreboard.getPlayer(), nextTitle);
            playerScoreboard.getObjective().setDisplayName(formattedTitle);
        }

        // Lines
        List<String> unformattedLines = pluginScoreboard.getLines();
        for (int index = unformattedLines.size(); index > 0; index--) {
            // First checks if index is in the set of scores that have to change, when not continues iterating
            if (!scoresToUpdate.contains(index)) continue;

            Team team = playerScoreboard.getScoreboard().getTeam("l" + index);
            String formattedLine = ChatUtil.parse(playerScoreboard.getPlayer(), unformattedLines.get(index));

            if (formattedLine.length() > 16) {
                String suffix = formattedLine.substring(0, 16);
                String prefix = formattedLine.substring(17);
                team.setSuffix(suffix);
                team.setPrefix(prefix);
            }
        }
    }

    public String getNextTitle() {
        List<String> titles = pluginScoreboard.getTitleLines();
        if (titles.isEmpty()) return null;
        String nextTitle = titles.get(titleIndex);
        titleIndex = (titleIndex + 1) % titles.size();
        return nextTitle;
    }

    public static List<String> generateEntriesBySize(int size) {
        Set<String> set = new HashSet<>();
        int colorsSize = ChatColor.values().length;
        for (int i = 0; i < 200; i++) {
            if (set.size() >= size) break;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i2 = 0; i2 < 2; i2++) {
                int randomColor = new Random().nextInt(colorsSize);
                ChatColor color = ChatColor.values()[randomColor];
                stringBuilder.append('§').append(color.getChar());
            }
            stringBuilder.append("§r");
            set.add(stringBuilder.toString());
        }
        return new ArrayList<>(set);
    }

}
