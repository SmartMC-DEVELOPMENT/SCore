package us.smartmc.lobbymodule.itemcommand;

import us.smartmc.core.SmartCore;
import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.util.PluginUtils;
import us.smartmc.lobbymodule.menu.SettingsMenu;
import us.smartmc.lobbymodule.util.PlayerUtil;

public class ConnectToAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        String name = args[0];
        PluginUtils.sendTo(clickHandler.player(), name);
    }
}
