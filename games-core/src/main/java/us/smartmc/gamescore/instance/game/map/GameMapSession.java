package us.smartmc.gamescore.instance.game.map;

import lombok.Getter;
import org.bukkit.Location;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

@Getter
public class GameMapSession {

    private final Game game;
    private final GameMap map;

    private BukkitCuboid cuboidReference;

    public GameMapSession(Game game, GameMap map) {
        this.game = game;
        this.map = map;
    }

    public void pasteMapRegionAt(Location location) {
        BackendConnection.getBackendConnection().ifPresent(backendConnection -> {
            backendConnection.getCuboid(map.getName()).thenAccept(res -> {
                CuboidWrapper wrapper = res.getWrapper();
                CuboidPaster paster = new CuboidPaster(wrapper);
                cuboidReference = paster.pasteAt(location);
            });
        });
    }

}
