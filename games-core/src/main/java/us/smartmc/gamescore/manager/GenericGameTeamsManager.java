package us.smartmc.gamescore.manager;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.gamescore.instance.game.team.ColorGameTeam;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;

@Setter
@Getter
public class GenericGameTeamsManager extends MapManager<String, GameTeam> {

    private boolean allyDamageAllowed = false;

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
