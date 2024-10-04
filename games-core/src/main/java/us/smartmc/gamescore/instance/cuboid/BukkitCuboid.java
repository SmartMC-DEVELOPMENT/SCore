package us.smartmc.gamescore.instance.cuboid;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Vector3i;

public class BukkitCuboid extends Cuboid {

    private final String worldName;

    public BukkitCuboid(Location loc1, Location loc2) throws IllegalArgumentException {
        super(locToIntVector(loc1), locToIntVector(loc2));
        this.worldName = loc1.getWorld().getName();

        if (!loc1.getWorld().equals(loc2.getWorld())) {
            throw new IllegalArgumentException("Las dos ubicaciones deben estar en el mismo mundo");
        }
    }

    public Vector3i getRelativeCoordinates(Location location) {
        return getRelativeCoordinates(locToIntVector(location));
    }

    public Location getGlobalLocation(Vector3i relativeCoords) {
        Vector3i coords =  getGlobalCoords(relativeCoords);
        return new Location(getWorld(), coords.x, coords.y, coords.z);
    }

    public Location getMinLocation() {
        return intVectorToLoc(getWorld(), super.getMin());
    }

    public Location getMaxLocation() {
        return intVectorToLoc(getWorld(), super.getMax());
    }

    public boolean contains(Location location) {
        return super.contains(locToIntVector(location));
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public static Location intVectorToLoc(World world, Vector3i vector3i) {
        return new Location(world, vector3i.x, vector3i.y, vector3i.z);
    }

    public static Vector3i locToIntVector(Location location) {
        return new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

}