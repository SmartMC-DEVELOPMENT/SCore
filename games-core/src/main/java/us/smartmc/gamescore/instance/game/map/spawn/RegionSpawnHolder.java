package us.smartmc.gamescore.instance.game.map.spawn;


import lombok.Getter;
import org.bson.Document;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.util.CuboidUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RegionSpawnHolder implements IRegionSpawnHolder {

    private final MapSpawnsData spawnsData;

    private final Map<String, Integer> indexes = new HashMap<>();

    public RegionSpawnHolder(MapSpawnsData data) {
        this.spawnsData = data;
    }

    @Override
    public Vector3i getNextPosition(GameTeam team, Vector3i minPositionReference) {
        String name = team == null ? null : team.getName();
        int nextIndex = indexes.getOrDefault(name, -1) + 1;
        return getRelativePosition(team, nextIndex, minPositionReference);
    }

    @Override
    public Vector3i getRelativePosition(GameTeam team, int index, Vector3i minPositionReference) {
        Vector3i min = getMinRelative(team);
        Vector3i max = getMaxRelative(team);
        Cuboid cuboid = new Cuboid(min, max);
        List<Vector3i> positionList = cuboid.getVectors();
        return positionList.get(index);
    }

    @Override
    public void setMinRelative(GameTeam team, Vector3i relativePosition) {
        getDocument(team).put("min", CuboidUtil.vectorToString(relativePosition));
        spawnsData.getMapData().save();
    }

    @Override
    public void setMaxRelative(GameTeam team, Vector3i relativePosition) {
        getDocument(team).put("max", CuboidUtil.vectorToString(relativePosition));
        spawnsData.getMapData().save();
    }

    @Override
    public Vector3i getMinRelative(GameTeam team) {
        return CuboidUtil.stringToVector(getDocument(team).getString("min"));
    }

    @Override
    public Vector3i getMaxRelative(GameTeam team) {
        return CuboidUtil.stringToVector(getDocument(team).getString("max"));
    }

    private Document getDocument(GameTeam team) {
        Document doc;
        if (team == null) {
            doc = spawnsData;
        } else {
            doc = (Document) spawnsData.getOrDefault(team.getName(), new Document());
        }
        return doc;
    }

}
