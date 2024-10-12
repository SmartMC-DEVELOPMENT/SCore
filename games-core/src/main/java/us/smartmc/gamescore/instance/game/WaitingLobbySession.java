package us.smartmc.gamescore.instance.game;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.util.CuboidUtil;

import java.util.function.Consumer;

@Getter
public class WaitingLobbySession {

    private final String name;
    private final Game game;

    private BukkitCuboid cuboidReference;

    public WaitingLobbySession(String name, Game game) {
        this.name = name;
        this.game = game;
    }

    public void clearCuboidRegion() {
        CuboidUtil.forEachBlock(cuboidReference, block -> {
            if (block.getType().equals(Material.AIR)) return;
            block.setType(Material.AIR);
        });
    }

    public void pasteRegionAt(Location location) {
        pasteRegionAt(location, null);
    }

    public void pasteRegionAt(Location location, Consumer<BukkitCuboid> action) {
        BackendConnection.getBackendConnection().ifPresent(backendConnection -> {
            EditMapSessionsManager editMapSessionsManager = EditMapSessionsManager.getManager(EditMapSessionsManager.class);
            if (editMapSessionsManager == null) return;
            backendConnection.getCuboid(editMapSessionsManager.getMapName(getBackendName(name))).thenAccept(res -> {
                if (res.getWrapper() == null) return;
                CuboidWrapper wrapper = res.getWrapper();
                CuboidPaster paster = new CuboidPaster(wrapper);
                cuboidReference = paster.pasteAt(location);

                {
                    // Action if not null
                    if (action == null) return;
                    Location min = BukkitCuboid.intVectorToLoc(location.getWorld(), wrapper.getMin());
                    Location max = BukkitCuboid.intVectorToLoc(location.getWorld(), wrapper.getMax());
                    action.accept(new BukkitCuboid(min, max));
                }
            });
        });
    }

    public static void saveWaitingLobbyRegion(String name, BukkitCuboid cuboid, Consumer<CuboidSaveResponse> consumer) {
        BackendConnection.getBackendConnection().ifPresent(backendConnection -> {
            backendConnection.sendCuboid(getBackendName(name), cuboid).thenAccept(consumer);
        });
    }

    private static String getBackendName(String name) {
        if (name.startsWith("waiting_lobby/")) return name;
        return "waiting_lobby/" + name;
    }

}
