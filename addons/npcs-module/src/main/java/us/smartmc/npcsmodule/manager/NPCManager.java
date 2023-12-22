package us.smartmc.npcsmodule.manager;

import us.smartmc.core.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.pluginsapi.language.Language;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import us.smartmc.npcsmodule.NPCSModule;
import us.smartmc.npcsmodule.instance.ManagerRegistry;
import us.smartmc.npcsmodule.instance.NPC;
import us.smartmc.npcsmodule.util.ConfigUtil;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class NPCManager extends ManagerRegistry<String, NPC> {

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
        put(name, new NPC(world, name));
    }

    public void register(NPC npc) {
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

    private List<NPC> loadNPCs() {
        List<NPC> list = new ArrayList<>();
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
            NPC npc = new NPC(location.getWorld(), name, skinValue, skinSignature);
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

    public NPC getNPC(int id) {
        for (Language language : Language.values()) {
            for (NPC npc : values()) {
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
