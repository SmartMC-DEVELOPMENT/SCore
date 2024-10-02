package us.smartmc.gamescore.instance.cuboid;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class BukkitCuboidConfig extends CuboidRegionConfig {

    private String worldReference;

    public BukkitCuboidConfig(String name) {
        super(name);
    }

    @Override
    public void loadRegionData() {
        super.loadRegionData();

        if (containsKey("world")) {
            this.worldReference = getString("world");
        }
    }

    public World getWorld(World fallbackWorld) {
        return worldReference == null ? fallbackWorld : Bukkit.getWorld(worldReference);
    }

}
