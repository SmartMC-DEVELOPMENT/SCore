package us.smartmc.game.instance;

import lombok.Getter;
import org.bukkit.Bukkit;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.skyblock.instance.island.ISkyBlockIsland;

import java.util.UUID;

@Getter
public class SkyBlockPlayerIsland implements ISkyBlockIsland {

    private final UUID islandId;
    private final UUID ownerId;

    private SkyBlockPlayerIslandData islandData;

    // Create Island
    public SkyBlockPlayerIsland(UUID ownerId) {
        this.islandId = UUID.randomUUID();
        this.ownerId = ownerId;
    }

    // Load Island
    public SkyBlockPlayerIsland(UUID islandId, UUID ownerId) {
        this.islandId = islandId;
        this.ownerId = ownerId;
    }

    // When loads from manager
    @Override
    public void register() {
        // Load Island data (DB info)
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> islandData = new SkyBlockPlayerIslandData(this));
    }

    // When unloads from manager
    @Override
    public void unregister() {
        islandData.saveData();
    }

    @Override
    public String displayName() {
        return null;
    }

    public SkyBlockPlayer getSkyBlockPlayer() {
        return SkyBlockPlayer.get(ownerId);
    }

    @Override
    public UUID getId() {
        return islandId;
    }
}
