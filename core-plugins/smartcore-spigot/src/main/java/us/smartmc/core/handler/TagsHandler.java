package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.kyori.adventure.text.Component;
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

import java.util.OptionalInt;

public class TagsHandler implements Listener {

    public TagsHandler() {
        if (areDisabled()) return;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(this::update);
        }, 0, 20 * 3);
    }

    public boolean areDisabled() {
        return SmartCore.getPlugin().getLobbyHandler().isDisabled("tagsEnabled");
    }

    private void update(Player player) {
        registerTagAboveHead(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event) {
        if (areDisabled()) return;
        Player player = event.getPlayer();
        registerTagAboveHead(player);
    }

    private void registerTagAboveHead(Player player) {
        Component formattedTag = getFormattedTag(player);
        player.displayName(formattedTag);
        player.playerListName(formattedTag);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = onlinePlayer.getScoreboard();
            String teamName = getUniqueTeamName(player);

            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                // Team creation
                team = scoreboard.registerNewTeam(teamName);
                Component prefix = PaperChatUtil.parseToComponent(player, "<chat.prefix.color>");
                team.prefix(prefix);
                if (!team.hasPlayer(player))
                    team.addPlayer(player);
            } else {
                // Team prefix update
                Component prefix = PaperChatUtil.parseToComponent(player, "<chat.prefix.color>");
                if (team.prefix().equals(prefix)) continue;
                team.prefix(prefix);
            }
        }
    }

    private Component getFormattedTag(Player player) {
        String unformattedTag = "<chat.prefix>&7<name>";
         return PaperChatUtil.parseToComponent(player, unformattedTag);
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
