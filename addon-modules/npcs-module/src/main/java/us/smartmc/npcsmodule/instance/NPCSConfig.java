package us.smartmc.npcsmodule.instance;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import org.bson.Document;
import us.smartmc.npcsmodule.NPCSModule;

import java.io.File;
import java.util.*;

public class NPCSConfig extends FilePluginConfig {

    public NPCSConfig() {
        super(new File(NPCSModule.getAddon().getDataFolder(),
                "npcs.json"));
        load();
        registerDefaults();
        save();
    }

    private void registerDefaults() {
        // IF NO KEYS DEFAULT VALUES:
        if (keySet().size() == 0) {
            String id = UUID.randomUUID().toString();
            HashMap<String, Object> defaultNPC = new HashMap<>();
            defaultNPC.put("enabled", true);
            defaultNPC.put("location", "world 0 0 0 0 0");
            defaultNPC.put("commands", Arrays.asList("test"));
            registerDefault(id, defaultNPC);
            save();
        }

        // DEFAULT NPC's CONFIG:
        for (String npcID : keySet()) {
            Object o = get(npcID);
            Document defaultNPC =
                    o.getClass().equals(HashMap.class) ?
                    new Document((Map<String, ?>) o) :
                    (Document) o;
            if (!defaultNPC.containsKey("nameVisible")) defaultNPC.put("nameVisible", true);
        }
    }

    public Set<String> getNpcIDs() {
        return keySet();
    }
}
