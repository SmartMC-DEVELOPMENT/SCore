package us.smartmc.core.itemcommands;

import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;

public class BungeeCommandAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        String name = label.split(" ")[0];
        label = label.replaceFirst(name + " ", "");
        SmartCorePlayer.get(clickHandler.player()).executeBungeeCordCommand("/" + label);
    }
}
