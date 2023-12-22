package us.smartmc.npcsmodule.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigUtil {

    public static String getLocationString(Location location, String regexSplit) {
        return location.getWorld().getName() + regexSplit +
                location.getX() + regexSplit +
                location.getY() + regexSplit +
                location.getZ() + regexSplit +
                location.getYaw() + regexSplit +
                location.getPitch();
    }

    public static Location getLocation(String locationString, String split) {
        String[] args = locationString.split(split);
        String world = args[0];
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        float yaw = Float.parseFloat(args[4]);
        float pitch = Float.parseFloat(args[5]);
        World bukkitWorld = Bukkit.getWorld(world) == null ?
                Bukkit.getWorlds().get(0) : Bukkit.getWorld(world);
        return new Location(bukkitWorld, x, y, z, yaw, pitch);
    }

}
