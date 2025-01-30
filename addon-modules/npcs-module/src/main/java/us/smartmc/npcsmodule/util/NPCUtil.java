package us.smartmc.npcsmodule.util;

import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.manager.NPCManager;

import java.util.Random;

public class NPCUtil {

    public static String getIdOrDefault(String id) {
        return id == null ? "npc" + new Random().nextInt(1000) : id;
    }

    public static String getNameOrDefault(String name) {
        return name == null ? "NPC-" + new Random().nextInt(1000) : name;
    }

    public static CustomNPC getDefaultCustomNPC(NPCManager manager, Location location, String id) throws Exception {
        if (location.getWorld() == null) throw new Exception("NPC could not created: Location world is null");

        Document data = new Document("nameVisible", true);
        data.put("enabled", true);
        data.put("location", ConfigUtil.getLocationString(location, " "));
        data.put("name", id);

        return new CustomNPC(manager,
                (((CraftWorld) location.getWorld()).getHandle()),
                getIdOrDefault(id),
                getNameOrDefault(id),
                data);
    }

}
