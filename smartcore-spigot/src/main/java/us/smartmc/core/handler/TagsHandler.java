package us.smartmc.core.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.smartmc.core.SmartCore;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
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

            Component chatPrefix = PaperChatUtil.parse(player, "<chat.prefix>");

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
