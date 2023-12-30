package us.smartmc.core.regions;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Consumer;

@Getter
public class Cuboid {

    private Location loc1, loc2;

    double minX, maxX, minY, maxY, minZ, maxZ;

    public Cuboid() {

    }

    public Cuboid(Location loc1, Location loc2) {
        setLoc1(loc1);
        setLoc2(loc2);
    }

    public void setLoc1(Location loc1) {
        loc1.setX(loc1.getBlockX());
        loc1.setY(loc1.getBlockY());
        loc1.setZ(loc1.getBlockZ());
        this.loc1 = loc1;
        if (loc2 != null) calculateMaxAndMins();
    }

    public void setLoc2(Location loc2) {
        loc2.setX(loc2.getBlockX());
        loc2.setY(loc2.getBlockY());
        loc2.setZ(loc2.getBlockZ());
        this.loc2 = loc2;
        if (loc1 != null) calculateMaxAndMins();
    }

    private void calculateMaxAndMins() {
        minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public void forEachBlock(Consumer<Location> blockConsumer) {
        World world = loc1.getWorld();
        assert world != null;
        if (!world.equals(loc2.getWorld())) {
            throw new IllegalArgumentException("Las localizaciones deben estar en el mismo mundo");
        }

        for (int x = (int) minX; x <= maxX; x++) {
            for (int y = (int) minY; y <= maxY; y++) {
                for (int z = (int) minZ; z <= maxZ; z++) {
                    Location blockLocation = new Location(world, x, y, z);
                    blockConsumer.accept(blockLocation);
                }
            }
        }
    }

    public boolean contains(Location location) {
        location.setX(location.getBlockX());
        location.setY(location.getBlockY());
        location.setZ(location.getBlockZ());
        return location.getX() >= minX && location.getX() <= maxX
                && location.getY() >= minY && location.getY() <= maxY
                && location.getZ() >= minZ && location.getZ() <= maxZ;
    }
}
