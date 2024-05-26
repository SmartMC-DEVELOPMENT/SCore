package us.smartmc.core.util;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigUtils {

    public static Document locationToDocument(Location location, boolean includeWorld, boolean includeYawAndPitch) {
        Document document = new Document();

        if (includeWorld) {
            document.append("world", location.getWorld().getName());
        }

        document.append("x", location.getX());
        document.append("y", location.getY());
        document.append("z", location.getZ());
        if (includeYawAndPitch) {
            document.append("yaw", location.getYaw())
                    .append("pitch", location.getPitch());
        }
        return document;
    }

    public static Location documentToLocation(Document document) {
        Location location =
                new Location(Bukkit.getWorld(
                        document.getString("world")),
                        document.get("x", Number.class).doubleValue(),
                        document.get("y", Number.class).doubleValue(),
                        document.get("z", Number.class).doubleValue());
        if (document.containsKey("yaw") && document.containsKey("pitch")) {
            location.setYaw(document.get("yaw", Number.class).floatValue());
            location.setPitch(document.get("pitch", Number.class).floatValue());
        }
        return location;
    }

    public static Location documentToLocation(World world, Document document) {
        Location location =
                new Location(world,
                        document.get("x", Number.class).doubleValue(),
                        document.get("y", Number.class).doubleValue(),
                        document.get("z", Number.class).doubleValue());
        if (document.containsKey("yaw") && document.containsKey("pitch")) {
            location.setYaw(document.get("yaw", Number.class).floatValue());
            location.setPitch(document.get("pitch", Number.class).floatValue());
        }
        return location;
    }

    public static void saveLocation(FilePluginConfig config, String path, Location location) {
        Document document = new Document();
        document.put("world", location.getWorld().getName());
        document.put("x", location.getX());
        document.put("y", location.getY());
        document.put("z", location.getZ());
        document.put("yaw", (double) location.getYaw());
        document.put("pitch", (double) location.getPitch());
        config.put(path, document);
        config.save();
    }

    public static Location loadLocation(FilePluginConfig config, String path) {
        try {
            Document document = config.get(path, Document.class);
            String worldName = document.getString("world");
            double x, y, z;
            x = document.getDouble("x");
            y = document.getDouble("y");
            z = document.getDouble("z");
            double yaw, pitch;
            yaw = document.get("yaw", Double.class);
            pitch = document.get("pitch", Double.class);
            return new Location(PluginUtils.getOrLoadWorld(worldName), x, y, z, (float) yaw, (float) pitch);
        } catch (Exception e) {
            System.out.println("Couldn't load a valid location from path " + path + " at config " + config.getFilePath());
        }
        return null;
    }

}
