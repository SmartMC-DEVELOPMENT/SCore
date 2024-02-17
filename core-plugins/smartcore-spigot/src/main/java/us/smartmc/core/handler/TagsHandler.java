package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
import java.util.OptionalInt;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void join(PlayerJoinEvent event) {
        if (areDisabled()) return;
        Player player = event.getPlayer();
        SyncUtil.later(() -> {
            registerTagAboveHead(player);
        }, 250);
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
        String unformattedTag = "<chat.prefix>&7<name>";
         return ChatUtil.parse(player, unformattedTag);
    }

    private String getUniqueTeamName(Player player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        String group = luckPerms.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        OptionalInt entry = luckPerms.getGroupManager().getGroup(group).getWeight();

        // Define un valor máximo de peso. Ajusta este valor según sea necesario.
        long maxWeight = 99999999L;

        // Calcula el valor inverso del peso para que un mayor peso resulte en un valor numérico más bajo.
        long invertedWeight = maxWeight - entry.orElse(0);

        // Combina el peso formateado con el nombre del grupo.

        return String.format("%016d", invertedWeight);
    }

}
