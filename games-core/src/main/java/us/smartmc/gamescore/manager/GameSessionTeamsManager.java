package us.smartmc.gamescore.manager;

import lombok.Setter;
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
                String formattedName = '§' + colorGameTeam.getColor().getCode() + player.getName();
                player.setDisplayName(formattedName);
            }
        });
        return super.put(uuid, teamName);
    }

    @Override
    public String getOrCreate(UUID uuid) {
        if (!containsKey(uuid)) {
            BukkitUtil.getPlayer(uuid).ifPresent(player -> {

            });

        }
        return super.getOrCreate(uuid);
    }

    private static GenericGameTeamsManager getGenericManager() {
        return MapManager.getManager(GenericGameTeamsManager.class);
    }

}
