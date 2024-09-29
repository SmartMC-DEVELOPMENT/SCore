package us.smartmc.gamescore.instance.cuboid;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Vector3i;

public class BukkitCuboidRegion extends CuboidRegion {

    public BukkitCuboidRegion(String name, Cuboid cuboid) {
        super(name, cuboid);
    }

    public BukkitCuboidRegion(String name) {
        super(name);
    }

    public BukkitCuboidRegion(CuboidRegion parent, String name) {
        super(parent, name);
    }

    private Location getMaxLocation(BukkitCuboidConfig config) {
        World world = config.getWorld(Bukkit.getWorlds().get(0));
        Vector3i max = cuboid.getMax();
        return new org.bukkit.Location(world, max.x, max.y, max.z);
    }

    private Location getMinLocation(BukkitCuboidConfig config) {
        World world = config.getWorld(Bukkit.getWorlds().get(0));
        Vector3i min = cuboid.getMin();
        return new org.bukkit.Location(world, min.x, min.y, min.z);
    }
}
