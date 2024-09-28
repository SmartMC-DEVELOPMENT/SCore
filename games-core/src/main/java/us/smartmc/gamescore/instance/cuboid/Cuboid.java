package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Vector3d;
import org.joml.Vector3i;

@Getter
public class Cuboid {

    private final Vector3i min, max;

    public Cuboid(Location loc1, Location loc2) {
        World world = loc1.getWorld();
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            throw new IllegalArgumentException("Las dos ubicaciones deben estar en el mismo mundo");
        }

        double xMin = Math.min(loc1.getX(), loc2.getX());
        double yMin = Math.min(loc1.getY(), loc2.getY());
        double zMin = Math.min(loc1.getZ(), loc2.getZ());

        double xMax = Math.max(loc1.getX(), loc2.getX());
        double yMax = Math.max(loc1.getY(), loc2.getY());
        double zMax = Math.max(loc1.getZ(), loc2.getZ());

        this.min = new Location(world, xMin, yMin, zMin);
        this.max = new Location(world, xMax, yMax, zMax);
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(min.getWorld())) {
            return false;
        }

        return loc.getX() >= min.getX() && loc.getX() <= max.getX() &&
                loc.getY() >= min.getY() && loc.getY() <= max.getY() &&
                loc.getZ() >= min.getZ() && loc.getZ() <= max.getZ();
    }

    public double getVolume() {
        return (max.getX() - min.getX() + 1) *
                (max.getY() - min.getY() + 1) *
                (max.getZ() - min.getZ() + 1);
    }

    public Location[] getCorners() {
        World world = min.getWorld();
        return new Location[] {
                new Location(world, min.getX(), min.getY(), min.getZ()),
                new Location(world, min.getX(), min.getY(), max.getZ()),
                new Location(world, min.getX(), max.getY(), min.getZ()),
                new Location(world, min.getX(), max.getY(), max.getZ()),
                new Location(world, max.getX(), min.getY(), min.getZ()),
                new Location(world, max.getX(), min.getY(), max.getZ()),
                new Location(world, max.getX(), max.getY(), min.getZ()),
                new Location(world, max.getX(), max.getY(), max.getZ())
        };
    }

}
