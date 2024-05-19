package us.smartmc.addon.holograms.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static Location stringToLocation(String location) {
        String[] split = location.split(" ");

        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String locationToString(Location location) {
        String locationS = "";

        locationS += location.getWorld().getName() + " ";
        locationS += location.getX() + " ";
        locationS += location.getY() + " ";
        locationS += location.getZ() + " ";
        locationS += location.getYaw() + " ";
        locationS += location.getPitch() + " ";

        return locationS;
    }

}