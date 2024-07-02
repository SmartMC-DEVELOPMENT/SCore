package us.smartmc.game.instance;

import org.bukkit.Bukkit;
import us.smartmc.game.SkyBlockPlugin;

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
    void setupIsland() {
        super.setupIsland();
    }
}
