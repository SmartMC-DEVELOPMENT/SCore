package us.smartmc.gamescore.instance.game.map.spawn;

import org.joml.Vector3i;
import us.smartmc.gamescore.instance.game.team.GameTeam;

public interface ISpawnHolder {

    void setPosition(GameTeam team, Vector3i relativePosition);
    Vector3i getRelativePosition(GameTeam team, Vector3i minPositionReference);

    MapSpawnsData getSpawnsData();
}
