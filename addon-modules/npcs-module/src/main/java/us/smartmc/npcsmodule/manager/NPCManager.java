package us.smartmc.npcsmodule.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import us.smartmc.npcsmodule.NPCSModule;
import us.smartmc.npcsmodule.instance.ManagerRegistry;
import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.util.ConfigUtil;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

@Getter
public class NPCManager extends ManagerRegistry<String, CustomNPC> {

    @Getter
    private static final List<NPCManager> managers = new ArrayList<>();

    @Getter
    private final FilePluginConfig config;

    public NPCManager() {
        managers.add(this);
        this.config = new FilePluginConfig(
                new File(NPCSModule.getAddon().getDataFolder(),
                        "npcs.json"));
        config.load();
        loadNPCsFromConfig();
        config.save();
    }

    public void register(String name, CustomNPC npc) {
        put(name, npc);

    }

    public void saveToConfig() {
        values().forEach(customNPC -> {
            config.put(customNPC.getConfigId(), customNPC.getConfigData());
        });
        config.save();
    }

    public CustomNPC delete(String name) {
        // ITERATE ALL ENTITIES THAT ARE THE SAME AS THE ORIGINAL REGISTERED NPC TO DELETE THEM
        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().stream().filter(e -> e.getUniqueId().equals(get(name).getUUID()))
                    .forEach(npc -> {
                        npc.remove();
                        npc.eject();
                    });
        });
        return remove(name);
    }

    public void loadNPCsFromConfig() {
        for (String key : config.keySet()) {
            Document data = config.get(key, Document.class);
            registerDefaultsToDoc(data);
            boolean enabled = data.get("enabled", true);
            if (!enabled) continue;
            String name = (String) data.get("name");
            Location location = ConfigUtil.getLocation(data.get("location").toString(), " ");
            String skinValue = null;
            if (data.containsKey("skinValue")) skinValue = data.getString("skinValue");
            String skinSignature = null;
            if (data.containsKey("skinSignature")) skinSignature = data.getString("skinSignature");
            // TO DO: HERE PARSE VARIABLES TO NPC INSTANCE AND REGISTER IT
            CustomNPC npc = new CustomNPC(this, ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(), key, name, data);

            if (skinValue != null) {
                npc.setSkin(skinValue, skinSignature);
            }

            System.out.println("Loaded npc " + key + " location = " + location);
            npc.setBukkitLocation(location);
            npc.setCommandLines(data.getList("commands", String.class, new ArrayList<>()));

            if (data.containsKey("nameVisible")) npc.setNameVisible(data.getBoolean("nameVisible"));

            register(name, npc);
        }
    }

    private void registerDefaultsToDoc(Document document) {
        if (!document.containsKey("name"))
            document.put("name", "Hello world!");
    }

    public CustomNPC getNPCByEntityId(int id) {
        for (Language language : Language.values()) {
            for (CustomNPC npc : values()) {
                if (npc == null) continue;
                if (npc.getNpcPlayer().getId() == id) return npc;
            }
        }
        return null;
    }

    public static void forEach(Consumer<NPCManager> consumer) {
        managers.forEach(consumer);
    }

}
