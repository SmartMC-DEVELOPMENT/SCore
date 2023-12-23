package us.smartmc.core.util;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigUtils {

    public static Document locationToDocument(Location location, boolean withYawPitch) {
        Document document = new Document("world", location.getWorld().getName())
                .append("x", location.getX())
                .append("y", location.getY())
                .append("z", location.getZ());
        if (withYawPitch) {
            document.append("yaw", location.getYaw())
                    .append("pitch", location.getPitch());
        }
        return document;
    }

    public static Location documentToLocation(Document document) {
        Location location =
                new Location(Bukkit.getWorld(
                        document.getString("world")),
                        document.getDouble("x"),
                        document.getDouble("y"),
                        document.getDouble("z"));
        if (document.containsKey("yaw") && document.containsKey("pitch")) {
            location.setYaw(document.getInteger("yaw").floatValue());
            location.setPitch(document.getInteger("pitch").floatValue());
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
