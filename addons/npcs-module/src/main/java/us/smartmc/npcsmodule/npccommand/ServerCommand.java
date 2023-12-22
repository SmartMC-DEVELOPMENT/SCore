package us.smartmc.npcsmodule.npccommand;

import us.smartmc.core.pluginsapi.util.SyncUtil;
import us.smartmc.core.util.PluginUtils;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.instance.NPCCommandExecutor;

public class ServerCommand implements NPCCommandExecutor {

    @Override
    public void onCommand(String name, String[] args, NPCUseEntityEvent event) {
        SyncUtil.later(() -> {
            PluginUtils.sendTo(event.getPlayer(), args[0]);
        }, 200);
    }
}
