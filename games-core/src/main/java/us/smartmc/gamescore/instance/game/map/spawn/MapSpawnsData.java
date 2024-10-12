package us.smartmc.gamescore.instance.game.map.spawn;

import lombok.Getter;
import org.bson.Document;
import us.smartmc.gamescore.instance.game.map.GameMapData;

import java.util.Optional;

@Getter
public class MapSpawnsData extends Document {

    private final GameMapData mapData;

    private Object holder;

    public MapSpawnsData(GameMapData mapData) {
        this.mapData = mapData;
        load();
    }

    public void load() {
        putAll((Document) mapData.getData().getOrDefault("spawns", new Document()));
        // Type load / register
        if (!containsKey("type")) {
            put("type", SpawnType.ONE_ALL.name());
        }

        // Create holder
        SpawnType type = getSpawnType();
        holder = switch (type) {
            case ONE_ALL, ONE_TEAM -> new OneSpawnHolder(this);
            case REGION_ALL, REGION_TEAM -> new RegionSpawnHolder(this);
            case LIST_ALL, LIST_TEAM -> new ListSpawnsHolder(this);
        };
    }

    public <T> Optional<T> getHolderAsOptional(Class<T> type) {
        if (type.isInstance(holder)) {
            return Optional.of(type.cast(holder));  // Uso de cast seguro
        }
        return Optional.empty();
    }

    public <T> T getHolderAs(Class<T> type) {
        if (type.isInstance(holder)) {
            return type.cast(holder);
        }
        return null;
    }

    public boolean toggleTeamType() {
        SpawnType type = getSpawnType();
        SpawnType newType = switch (type) {
            case ONE_ALL -> SpawnType.ONE_TEAM;
            case ONE_TEAM -> SpawnType.ONE_ALL;
            case REGION_ALL -> SpawnType.REGION_TEAM;
            case REGION_TEAM -> SpawnType.REGION_ALL;
            case LIST_ALL -> SpawnType.LIST_TEAM;
            case LIST_TEAM -> SpawnType.LIST_ALL;
        };
        put("type", newType.name());
        return isTypeForTeam();
    }

    public boolean isTypeForTeam() {
        return getSpawnType().name().endsWith("_TEAM");
    }

    public SpawnType getSpawnType() {
        return SpawnType.valueOf(getString("type"));
    }

    public void save() {
        mapData.set("spawns", this);
    }

}
