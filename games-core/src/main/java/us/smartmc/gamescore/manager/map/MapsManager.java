package us.smartmc.gamescore.manager.map;

import lombok.Getter;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.manager.MapManager;

@Getter
public class MapsManager extends MapManager<String, GameMap> {

    @Getter
    private final MapsGridManager gridManager;
    private final String databaseId, collectionId;

    public MapsManager() {
        this.databaseId = "game_maps";
        this.collectionId = GamesCoreAPI.getApi().getGameId();
        this.gridManager = new MapsGridManager(this);
    }

    @Override
    public GameMap createValueByKey(String name) {
        return new GameMap(name, this);
    }
}
