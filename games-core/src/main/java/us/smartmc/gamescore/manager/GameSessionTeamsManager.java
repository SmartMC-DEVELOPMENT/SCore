package us.smartmc.gamescore.manager;

import lombok.Setter;
import org.bukkit.entity.Player;
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

    public String remove(Player player) {
        String oldName = oldNames.get(player.getUniqueId());
        if (oldName != null) {
            player.setPlayerListName(oldName);
        }
        return remove(player.getUniqueId());
    }

    public GameTeam getGameTeam(Player player) {
        UUID uuid = player.getUniqueId();
        String teamName = get(uuid);
        return getGenericManager().getOrCreate(teamName);
    }

    @Override
    public String getOrCreate(UUID uuid) {
        if (!containsKey(uuid)) {
            BukkitUtil.getPlayer(uuid).ifPresent(player -> {
                String name = player.getPlayerListName();
                oldNames.put(uuid, name);
            });
        }
        return super.getOrCreate(uuid);
    }

    private static GenericGameTeamsManager getGenericManager() {
        return MapManager.getManager(GenericGameTeamsManager.class);
    }

}
