package us.smartmc.gamescore.instance.game.map.spawn;


import lombok.Getter;
import org.bson.Document;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.util.CuboidUtil;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ListSpawnsHolder implements ISpawnsHolder {

    private final MapSpawnsData spawnsData;

    private final Map<String, Integer> indexes = new HashMap<>();

    public ListSpawnsHolder(MapSpawnsData data) {
        this.spawnsData = data;
    }

    @Override
    public void addPosition(GameTeam team, Vector3i relativePosition) {
        List<Vector3i> positions = getPositions(team);
        positions.add(relativePosition);
        savePositions(team, positions);
    }

    @Override
    public Vector3i getRelativePosition(GameTeam team, int index, Vector3i minPositionReference) {
        List<Vector3i> positions = getPositions(team);
        return positions.get(index);
    }

    @Override
    public List<Vector3i> getPositions(GameTeam team) {
        List<Vector3i> list = new ArrayList<>();
        Document document = getDocument(team);
        for (String str : document.getList("spawns", String.class)) {
            list.add(CuboidUtil.stringToVector(str));
        }
        return list;
    }

    @Override
    public void savePositions(GameTeam team, List<Vector3i> posList) {
        List<String> list = new ArrayList<>();
        for (Vector3i vector3i : posList) {
            list.add(CuboidUtil.vectorToString(vector3i));
        }
        getDocument(team).put("spawns", list);
        spawnsData.getMapData().save();
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
