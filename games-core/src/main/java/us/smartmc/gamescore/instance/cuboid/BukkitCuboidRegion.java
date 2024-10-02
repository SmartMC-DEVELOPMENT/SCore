package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.joml.Vector3i;
import us.smartmc.gamescore.util.CuboidUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class BukkitCuboidRegion extends CuboidRegion {

    protected final BukkitCuboid cuboid;
    private final BukkitCuboidConfig config;

    public BukkitCuboidRegion(String name, BukkitCuboid cuboid) {
        super(name, cuboid);
        this.config = new BukkitCuboidConfig(name, cuboid.getWorld());
        this.cuboid = cuboid;
    }

    public BukkitCuboidRegion(String name) {
        super(name);
        this.config = new BukkitCuboidConfig(name);
        this.cuboid = new BukkitCuboid(getMinLocation(config), getMaxLocation(config));
    }

    public Iterator<BlockVector> iterator() {
        List<BlockVector> list = new ArrayList<>();
        for (Block block : CuboidUtil.getBlocks(getCuboid().getWorld(), cuboid)) {
            list.add(new BlockVector(block.getX(), block.getY(), block.getZ()));
        }
        return list.listIterator();
    }

    private Location getMaxLocation(BukkitCuboidConfig config) {
        World world = config.getWorld(Bukkit.getWorlds().get(0));
        Vector3i max = defaultCuboid.getMax();
        return new org.bukkit.Location(world, max.x, max.y, max.z);
    }

    private Location getMinLocation(BukkitCuboidConfig config) {
        World world = config.getWorld(Bukkit.getWorlds().get(0));
        Vector3i min = defaultCuboid.getMin();
        return new org.bukkit.Location(world, min.x, min.y, min.z);
    }
}
