package us.smartmc.addon.holograms.util;

import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.manager.NPCManager;

import java.util.*;

public class NPCModuleUtil {

    public static CustomNPC getFirstByName(String name) {
        List<CustomNPC> list = new ArrayList<>(getByName(name));
        return list.isEmpty() ? null : list.get(0);
    }

    public static Collection<CustomNPC> getByName(String name) {
        Set<CustomNPC> list = new HashSet<>();
        NPCManager.getManagers().forEach(manager -> {
            for (CustomNPC npc : manager.values()) {
                if (npc.getName().equals(name)) {
                    list.add(npc);
                }
            }
        });
        return list;
    }

}
