package us.smartmc.npcsmodule.manager;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.npcsmodule.NPCSModule;
import us.smartmc.npcsmodule.instance.ManagerRegistry;
import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.util.ConfigUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class NPCManager extends ManagerRegistry<String, CustomNPC> {

    private static final Set<NPCManager> managers = new HashSet<>();

    private final FilePluginConfig config;

    public NPCManager() {
        managers.add(this);
        this.config = new FilePluginConfig(
                new File(NPCSModule.getAddon().getDataFolder(),
                        "npcs.json"));
        config.load();
        loadNPCs();
        config.save();
    }

    public void register(World world, String name) {
        put(name, new CustomNPC(world, name));
    }

    public void register(CustomNPC npc) {
        register(npc.getName(), npc);
    }

    public void delete(String name) {
        // ITERATE ALL ENTITIES THAT ARE THE SAME AS THE ORIGINAL REGISTERED NPC TO DELETE THEM
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().stream().filter(e -> e.getUniqueId().equals(get(name).getUUID()))
                    .forEach(npc -> {
                        npc.remove();
                        npc.eject();
                    });
        });
        remove(name);
    }

    private List<CustomNPC> loadNPCs() {
        List<CustomNPC> list = new ArrayList<>();
        for (String key : config.keySet()) {
            Document data = config.get(key, Document.class);
            registerDefaultsToDoc(data);
            boolean enabled = (boolean) data.get("enabled");
            if (!enabled) continue;
            String name = (String) data.get("name");
            List<String> lines = (List<String>) data.get("lines");
            Location location = ConfigUtil.getLocation(data.get("location").toString(), " ");
            String skinValue = null;
            if (data.containsKey("skinValue")) skinValue = data.getString("skinValue");
            String skinSignature = null;
            if (data.containsKey("skinSignature")) skinSignature = data.getString("skinSignature");
            // TO DO: HERE PARSE VARIABLES TO NPC INSTANCE AND REGISTER IT
            CustomNPC npc = new CustomNPC(location.getWorld(), name, skinValue, skinSignature);
            npc.setLocation(location);
            npc.setCommandLines(data.getList("commands", String.class));

            if (data.containsKey("nameVisible")) npc.setNameVisible(data.getBoolean("nameVisible"));

            if (lines != null) npc.setLines(lines);

            register(npc);
            list.add(npc);
        }
        return list;
    }

    private void registerDefaultsToDoc(Document document) {
        if (!document.containsKey("name"))
            document.put("name", "Hello world!");
    }

    public FilePluginConfig getConfig() {
        return config;
    }

    public CustomNPC getNPC(int id) {
        for (Language language : Language.values()) {
            for (CustomNPC npc : values()) {
                if (npc == null) continue;
                if (npc.getEntityPlayer().getId() == id) return npc;
            }
        }
        return null;
    }

    public static void forEach(Consumer<NPCManager> consumer) {
        managers.forEach(consumer);
    }

}
