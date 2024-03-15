package us.smartmc.npcsmodule.npccommand;

import me.imsergioh.pluginsapi.util.PluginUtils;
import me.imsergioh.pluginsapi.util.SyncUtil;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.instance.NPCCommandExecutor;

public class ServerCommand implements NPCCommandExecutor {

    @Override
    public void onCommand(String name, String[] args, NPCUseEntityEvent event) {
        PluginUtils.redirectTo(event.getPlayer(), args[0]);
    }
}
