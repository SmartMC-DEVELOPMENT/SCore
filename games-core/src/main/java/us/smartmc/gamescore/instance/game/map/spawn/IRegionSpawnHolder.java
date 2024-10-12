package us.smartmc.gamescore.instance.game.map.spawn;

import org.joml.Vector3i;
import us.smartmc.gamescore.instance.game.team.GameTeam;

public interface IRegionSpawnHolder {

    Vector3i getNextPosition(GameTeam team, Vector3i minPositionReference);

    Vector3i getRelativePosition(GameTeam team, int index, Vector3i minPositionReference);

    void setMinRelative(GameTeam team, Vector3i relativePosition);
    void setMaxRelative(GameTeam team, Vector3i relativePosition);

    Vector3i getMinRelative(GameTeam team);
    Vector3i getMaxRelative(GameTeam team);

}
