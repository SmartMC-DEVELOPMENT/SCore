package us.smartmc.game.instance;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.manager.IslandsSchematicsManager;
import us.smartmc.skyblock.instance.island.ISkyBlockIsland;

import java.util.UUID;

@Getter
public class SkyBlockPlayerIsland implements ISkyBlockIsland {

    private final UUID islandId;
    private final UUID ownerId;

    protected SkyBlockPlayerIslandData islandData;

    boolean createdIsland;

    // Create Island
    public SkyBlockPlayerIsland(UUID ownerId) {
        this.islandId = UUID.randomUUID();
        this.ownerId = ownerId;
        createdIsland = true;
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
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            islandData = new SkyBlockPlayerIslandData(this);
            Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), this::setupIsland);
        });
    }

    void setupIsland() {
        World world = createIslandWorld();
        world.setSpawnLocation(0, 70, 0);
        Location spawn = world.getSpawnLocation();
        Location min = spawn.clone().add(-30, -30, -30);
        Location max = spawn.clone().add(30, 30, 30);
        islandData.setupIslandData(min, max, spawn);
        SkyBlockPlayer skyBlockPlayer = getSkyBlockPlayer();
        if (skyBlockPlayer != null && skyBlockPlayer.getBukkitPlayer() != null) {
            Player player = skyBlockPlayer.getBukkitPlayer();

            Bukkit.getScheduler().runTaskAsynchronously(SkyBlockPlugin.getPlugin(), () -> {
                // Load from island set or default one if not set!
                UUID islandIdToGenerate =  getSkyBlockPlayer().getPlayerData().getIslandSetId();
                if (createdIsland) islandIdToGenerate = IslandsSchematicsManager.getDefaultIslandId();


                IslandsSchematicsManager.loadAndPasteSchematic(world, islandIdToGenerate);

                Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
                    player.teleport(spawn.clone().add(0.5, 0.5, 0.5));
                });
            });


        }
    }

    // When unloads from manager
    @Override
    public void unregister() {
        islandData.saveData();
        try {
            IslandsSchematicsManager.saveRegion(getWorld(), this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private World createIslandWorld() {
        WorldCreator worldCreator = new WorldCreator(getIslandWorldName(islandId));
        worldCreator.generator(new EmptyChunkGenerator());
        return worldCreator.createWorld();
    }

    private World getWorld() {
        return Bukkit.getWorld(getIslandWorldName(islandId));
    }

    private static String getIslandWorldName(UUID id) {
        return "island-" + id.toString();
    }

}
