package us.smartmc.npcsmodule.manager;

import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.instance.CustomNPC;
import us.smartmc.npcsmodule.instance.NPCCommandExecutor;

import java.util.HashMap;
import java.util.List;

public class NPCCommandManager {

    private static final HashMap<String, NPCCommandExecutor> executors = new HashMap<>();

    public static void register(String name, NPCCommandExecutor executor) {
        executors.put(name, executor);
    }

    public static void performCommand(NPCUseEntityEvent event) {
        CustomNPC npc = event.getNPC();
        List<String> commands = npc.getCommandLines();
        if (commands.isEmpty()) return;
        for (String label : commands) {
            String name = label.split(" ")[0];
            NPCCommandExecutor executor = executors.get(name);
            if (executor == null) continue;
            String[] args = label.replaceFirst(name + " ", "").split(" ");
            executors.get(name).onCommand(name, args, event);
        }
    }

    public static void unregister(String name) {
        executors.remove(name);
    }

}
