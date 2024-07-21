package us.smartmc.game.instance;

import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.manager.IslandsSchematicsManager;

import java.util.UUID;

public class DefaultIsland extends SkyBlockPlayerIsland {



    public DefaultIsland(UUID islandId) {
        super(islandId, null);
    }

    @Override
    public void register() {
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            islandData = new SkyBlockPlayerIslandData(this);
            if (islandData.hasIslandReference()) return;
            Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), this::setupIsland);
        });
    }

    @Override
    public void setupIsland() {
        SlimeWorld world = null;
        try {
            world = IslandsSchematicsManager.getDefaultIslandWorld();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Bukkit.getScheduler().runTaskAsynchronously(SkyBlockPlugin.getPlugin(), () -> {
            // Load from island set or default one if not set!
            UUID islandIdToGenerate = IslandsSchematicsManager.getDefaultIslandId();
            try {
                IslandsSchematicsManager.createIslandWorld(islandIdToGenerate);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public SlimeWorld getWorld() throws Exception {
        return IslandsSchematicsManager.getDefaultIslandWorld();
    }

}
