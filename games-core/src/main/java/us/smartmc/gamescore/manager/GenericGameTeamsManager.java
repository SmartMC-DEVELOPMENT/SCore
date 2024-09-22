package us.smartmc.gamescore.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.game.team.ColorGameTeam;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.listener.GameTeamsManagerListeners;

@Getter @Setter
public class GenericGameTeamsManager extends MapManager<String, GameTeam> {

    private boolean allyDamageAllowed = false;

    public GenericGameTeamsManager() {
        Bukkit.getPluginManager().registerEvents(new GameTeamsManagerListeners(), GamesCoreAPI.getApi().getPlugin());
    }

    public ColorGameTeam getGameTeam(String name) {
        GameTeam team = get(name);
        if (team == null) return null;
        if (team instanceof ColorGameTeam colorGameTeam) return colorGameTeam;
        return null;
    }

    @Override
    public GameTeam createValueByKey(String name) {
        return new GameTeam(name);
    }
}
