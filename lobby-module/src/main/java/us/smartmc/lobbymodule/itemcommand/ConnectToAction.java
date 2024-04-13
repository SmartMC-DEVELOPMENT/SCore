package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.util.PluginUtils;

public class ConnectToAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        String name = args[0];
        PluginUtils.redirectTo(clickHandler.player(), name);
    }
}
