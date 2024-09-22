package us.smartmc.gamescore.manager;

import lombok.Setter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class GameSessionTeamsManager extends MapManager<UUID, String> {

    @Setter
    private static String defaultTeamName = "default";

    @Override
    public String createValueByKey(UUID key) {
        return defaultTeamName;
    }

    public String remove(Player player) {
        return remove(player.getUniqueId());
    }

    public GameTeam getGameTeam(Player player) {
        UUID uuid = player.getUniqueId();
        String teamName = get(uuid);
        return getGenericManager().getOrCreate(teamName);
    }

    private static GenericGameTeamsManager getGenericManager() {
        return MapManager.getManager(GenericGameTeamsManager.class);
    }

}
