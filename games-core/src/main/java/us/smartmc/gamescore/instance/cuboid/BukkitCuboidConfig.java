package us.smartmc.gamescore.instance.cuboid;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class BukkitCuboidConfig extends CuboidRegionConfig {

    private String worldReference;

    public BukkitCuboidConfig(String name) {
        super(name);
    }

    @Override
    public void loadRegionData(String key) {
        super.loadRegionData(key);
        String path = key == null ? "" : SUBREGIONS_KEY + "." + key;

        String worldKey = getKey(path, "world");
        if (containsKey(worldKey)) {
            this.worldReference = getString(worldKey);
        }
    }

    public World getWorld(World fallbackWorld) {
        return worldReference == null ? fallbackWorld : Bukkit.getWorld(worldReference);
    }

}
