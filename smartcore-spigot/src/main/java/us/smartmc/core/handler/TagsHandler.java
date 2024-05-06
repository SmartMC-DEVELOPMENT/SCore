package us.smartmc.core.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.smartmc.core.SmartCore;

import java.util.OptionalInt;

public class TagsHandler implements Listener {

    private Objective objective;

    public TagsHandler() {
        if (areDisabled()) return;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartCore.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(this::update);
        }, 0, 20 * 3);


        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(SmartCore.getPlugin(), PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {

            }
        });

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
        player.customName(formattedTag);
        player.setCustomNameVisible(true);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = onlinePlayer.getScoreboard();
            String teamName = getUniqueTeamName(player);

            Team team = scoreboard.getTeam(teamName);

            Component chatPrefix = null;

            if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
                chatPrefix = PaperChatUtil.parse(player, "<chat.prefix>");
            } else {
                chatPrefix = Component.empty();
            }

            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }
            team.prefix(chatPrefix);
            team.suffix(Component.empty());
            team.color(NamedTextColor.GRAY);
            if (!team.hasPlayer(player))
                team.addPlayer(player);
        }
    }

    private Component getFormattedTag(Player player) {
        String unformattedTag = "<chat.prefix><reset><gray><name>";
         return PaperChatUtil.parse(player, unformattedTag);
    }

    private String getUniqueTeamName(Player player) {
        return new StringBuilder(player.getUniqueId().toString()).substring(0, 12);
    }
}
