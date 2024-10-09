package us.smartmc.gamescore.instance.game.map;


import lombok.Getter;
import us.smartmc.gamescore.manager.map.MapsManager;

@Getter
public class GameMap {

    private final String name;
    private final GameMapData data;

    public GameMap(String name, MapsManager manager) {
        this.name = name;
        this.data = new GameMapData(this, manager);
    }

    public boolean isEnabled() {
        return data.isEnabled();
    }

}
