package us.smartmc.gamescore.instance.game.map.spawn;

import org.joml.Vector3i;
import us.smartmc.gamescore.instance.game.team.GameTeam;

import java.util.List;

public interface ISpawnsHolder {

    void addPosition(GameTeam team, Vector3i relativePosition);
    Vector3i getRelativePosition(GameTeam team, int index, Vector3i minPositionReference);

    void savePositions(GameTeam team, List<Vector3i> posList);
    List<Vector3i> getPositions(GameTeam team);

    MapSpawnsData getSpawnsData();
}
