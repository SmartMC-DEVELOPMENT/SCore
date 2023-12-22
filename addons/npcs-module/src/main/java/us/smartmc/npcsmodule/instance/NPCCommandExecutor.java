package us.smartmc.npcsmodule.instance;

import us.smartmc.npcsmodule.event.NPCUseEntityEvent;

public interface NPCCommandExecutor {

    void onCommand(String name, String[] args, NPCUseEntityEvent event);

}
