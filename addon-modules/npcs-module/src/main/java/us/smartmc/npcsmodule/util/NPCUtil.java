package us.smartmc.npcsmodule.util;

import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.npcsmodule.instance.CustomNPC;

import java.util.Random;

public class NPCUtil {

    public static String getIdOrDefault(String id) {
        return id == null ? "npc" + new Random().nextInt(1000) : id;
    }

    public static String getNameOrDefault(String name) {
        return name == null ? "NPC-" + new Random().nextInt(1000) : name;
    }

    public static CustomNPC getDefaultCustomNPC(Location location, String id) throws CorePluginException {
        if (location.getWorld() == null) throw new CorePluginException("NPC could not created: Location world is null");

        return new CustomNPC(((CraftWorld) location.getWorld()).getHandle(),
                getIdOrDefault(id),
                getNameOrDefault(id),
                null, null,
                new Document("nameVisible", true));
    }

}
