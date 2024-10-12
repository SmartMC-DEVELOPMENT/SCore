package us.smartmc.gamescore.instance.game.map.spawn;


import lombok.Getter;
import org.bson.Document;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.util.CuboidUtil;

@Getter
public class OneSpawnHolder implements ISpawnHolder {

    private final MapSpawnsData spawnsData;

    public OneSpawnHolder(MapSpawnsData data) {
        this.spawnsData = data;
    }

    @Override
    public void setPosition(GameTeam team, Vector3i relativePosition) {
        String strPosition = CuboidUtil.vectorToString(relativePosition);
        if (team == null) {
            // Global
            spawnsData.put("position", strPosition);
            return;
        }

        // Team
        String name = team.getName();
        spawnsData.put(name, strPosition);
        spawnsData.getMapData().save();
    }

    @Override
    public Vector3i getRelativePosition(GameTeam team, Vector3i minPositionReference) {
        Vector3i relativePosition;
        if (team == null) {
            relativePosition = CuboidUtil.stringToVector(spawnsData.getString("position"));
        } else {
            relativePosition = CuboidUtil.stringToVector(spawnsData.getString(team.getName()));
        }
        return relativePosition;
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
