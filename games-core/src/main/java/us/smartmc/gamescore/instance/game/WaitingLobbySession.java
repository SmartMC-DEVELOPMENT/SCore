package us.smartmc.gamescore.instance.game;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.joml.Vector3i;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.backend.instance.config.IDataConfig;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.gamescore.instance.storage.SBackendData;
import us.smartmc.gamescore.util.CuboidUtil;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Getter
public class WaitingLobbySession {

    private final String name;
    private final Game game;

    private BukkitCuboid cuboidReference;

    @Getter
    private Location spawn;

    public WaitingLobbySession(String name, Game game) {
        this.name = name;
        this.game = game;
    }


    private void requestSpawn() {
        BackendConnection.getBackendConnection().ifPresent(connection -> {
            connection.getConfig("/waitingLobby_spawns/" + name).thenAccept(config -> {
                Vector3i vector = CuboidUtil.stringToVector((String) config.get("spawn"));
                spawn = cuboidReference.getGlobalLocation(vector);
            });
        });
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
            backendConnection.getCuboid(getBackendName(name)).thenAccept(res -> {
                if (res.getWrapper() == null) return;
                CuboidWrapper wrapper = res.getWrapper();
                CuboidPaster paster = new CuboidPaster(wrapper);

                Vector3i wrapperMin = wrapper.getMin();
                Vector3i wrapperMax = wrapper.getMax();
                Cuboid cuboid = new Cuboid(wrapperMin, wrapperMax);
                int width = cuboid.getWidth();
                int depth = cuboid.getDepth();

                cuboidReference = paster.pasteAt(location.clone().add(-width / 2, 0, -depth / 2));
                requestSpawn();
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

    public static void saveWaitingLobbyRegion(String name, Location spawn, BukkitCuboid cuboid, Consumer<CuboidSaveResponse> consumer) {

        SBackendData data = new SBackendData("/waitingLobby_spawns/" + name);
        Vector3i relativeSpawn = cuboid.getRelativeCoordinates(spawn);
        data.put("spawn", CuboidUtil.vectorToString(relativeSpawn));
        data.save();

        BackendConnection.getBackendConnection().ifPresent(backendConnection -> {
            backendConnection.sendCuboid(getBackendName(name), cuboid).thenAccept(consumer);
        });
    }

    private static String getBackendName(String name) {
        if (name.startsWith("waiting_lobby/")) return name;
        return "waiting_lobby/" + name;
    }

}
