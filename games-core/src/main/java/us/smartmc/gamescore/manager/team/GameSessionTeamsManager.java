package us.smartmc.gamescore.manager.team;

import lombok.Setter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.game.team.ColorGameTeam;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSessionTeamsManager extends MapManager<UUID, String> {

    private static final Map<UUID, String> oldNames = new HashMap<>();

    @Setter
    private static String defaultTeamName = "default";

    @Override
    public String createValueByKey(UUID key) {
        return defaultTeamName;
    }

    @Override
    public String remove(Object key) {

        // Rollback name if oldNames contains name
        if (key instanceof UUID id) {
            BukkitUtil.getPlayer(id).ifPresent(player -> {
                String oldName = oldNames.get(player.getUniqueId());
                if (oldName == null) return;
                player.setDisplayName(oldName);
                player.setPlayerListName(oldName);
            });
        }
        return super.remove(key);
    }

    public GameTeam getGameTeam(Player player) {
        UUID uuid = player.getUniqueId();
        String teamName = get(uuid);
        return getGenericManager().getOrCreate(teamName);
    }

    @Override
    public String put(UUID uuid, String teamName) {
        BukkitUtil.getPlayer(uuid).ifPresent(player -> {
            // Storage name for rollback later
            String name = player.getDisplayName();
            oldNames.put(uuid, name);
            // Set displayName with color and playerName
            GameTeam team = getGenericManager().getGameTeam(teamName);
            if (team instanceof ColorGameTeam colorGameTeam) {
                String playerName = '&' + String.valueOf(colorGameTeam.getColor().getCode()) + player.getName() + "&r";
                player.setDisplayName(ChatUtil.color(playerName));
                player.setPlayerListName(ChatUtil.color(playerName));
                player.sendMessage(ChatUtil.color(playerName));
            }
        });
        return super.put(uuid, teamName);
    }

    private static GenericGameTeamsManager getGenericManager() {
        return MapManager.getManager(GenericGameTeamsManager.class);
    }

}
