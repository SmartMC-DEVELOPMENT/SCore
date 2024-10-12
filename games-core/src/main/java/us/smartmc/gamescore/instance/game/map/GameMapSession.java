
package us.smartmc.gamescore.instance.game.map;


import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.manager.map.MapsGridManager;
import us.smartmc.gamescore.manager.map.MapsManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter
public class GameMapSession {

    private static final int BORDER_ADDITION = 20;

    private static MapsGridManager gridManager;

    private final Game game;
    private final GameMap map;

    private BukkitCuboid cuboidReference, borderReference;
    private int xReferenceGrid = -1;

    private final Set<Consumer<BukkitCuboid>> cuboidReferenceFutures = new HashSet<>();

    public GameMapSession(Game game, GameMap map) {
        this.game = game;
        this.map = map;
    }

    public Location pasteAtReferenceGridLocPoint(World world) {
        Location referenceLocationPoint = Objects.requireNonNull(getGridManager())
                .getLocationReferencePoint(world);
        pasteMapRegionAt(referenceLocationPoint);
        xReferenceGrid = referenceLocationPoint.getBlockX();

        return referenceLocationPoint;
    }

    public void liberateReferenceLocationIfSet() {
        if (xReferenceGrid == -1) return;
        Objects.requireNonNull(getGridManager())
                .setAvailableIndex(xReferenceGrid);
        xReferenceGrid = -1;
    }

    public void pasteMapRegionAt(Location location) {
        BackendConnection.getBackendConnection().ifPresent(backendConnection -> {
            EditMapSessionsManager editMapSessionsManager = EditMapSessionsManager.getManager(EditMapSessionsManager.class);
            if (editMapSessionsManager == null) return;
            backendConnection.getCuboid(editMapSessionsManager.getMapName(map.getName())).thenAccept(res -> {
                CuboidWrapper wrapper = res.getWrapper();
                CuboidPaster paster = new CuboidPaster(wrapper);
                cuboidReference = paster.pasteAt(location);

                // Accept consumers & clear list
                for (Consumer<BukkitCuboid> future : cuboidReferenceFutures) {
                    future.accept(cuboidReference);
                }
                cuboidReferenceFutures.clear();

                borderReference = new BukkitCuboid(cuboidReference.getMinLocation().clone().add(-BORDER_ADDITION, -BORDER_ADDITION, -BORDER_ADDITION),
                        cuboidReference.getMaxLocation().clone().add(BORDER_ADDITION, BORDER_ADDITION, BORDER_ADDITION));
            });
        });
    }

    public void whenPastedRegion(Consumer<BukkitCuboid> future) {
        cuboidReferenceFutures.add(future);
    }

    private static MapsGridManager getGridManager() {
        if (gridManager == null) {
            MapsManager mapsManager = MapsManager.getManager(MapsManager.class);
            if (mapsManager == null) return null;
            gridManager = mapsManager.getGridManager();
        }
        return gridManager;
    }

}
