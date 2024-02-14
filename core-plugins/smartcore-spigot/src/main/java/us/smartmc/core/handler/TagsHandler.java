package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.smartmc.core.SmartCore;

import java.util.HashMap;

public class TagsHandler implements Listener {

    private static final HashMap<Player, String> tags = new HashMap<>();

    public TagsHandler() {
        if (areDisabled()) return;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(this::checkUpdate);
        }, 0, 20 * 3);
    }

    public boolean areDisabled() {
        return SmartCore.getPlugin().getLobbyHandler().isDisabled("tagsEnabled");
    }

    private void checkUpdate(Player player) {
        String tag = getFormattedTag(player);
        String lastTagKnown = tags.get(player);
        if (!tag.equals(lastTagKnown)) registerTagAboveHead(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event) {
        if (areDisabled()) return;
        Player player = event.getPlayer();
        SyncUtil.sync(() -> {
            registerTagAboveHead(player);
        });
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        tags.remove(event.getPlayer());
    }

    private void registerTagAboveHead(Player player) {
        String formattedTag = getFormattedTag(player);
        player.setPlayerListName(formattedTag);
        player.setDisplayName(formattedTag);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = onlinePlayer.getScoreboard();
            String teamName = getUniqueTeamName(player);
            Team team = scoreboard.getTeam(teamName) == null ?
                    scoreboard.registerNewTeam(teamName) : scoreboard.getTeam(teamName);
            team.setPrefix(ChatUtil.parse(player, "<chat.prefix.color>"));
            team.addPlayer(player);
        }
    }

    private String getFormattedTag(Player player) {
        String unformattedTag = "<chat.prefix>&f<name>";
        if (unformattedTag == null) {

        }
        return ChatUtil.parse(player, unformattedTag);
    }

    private String getUniqueTeamName(Player player) {
        String entry = player.getUniqueId().toString().substring(0, 7);
        entry += player.getName().substring(0, 7);
        return entry;
    }

}
