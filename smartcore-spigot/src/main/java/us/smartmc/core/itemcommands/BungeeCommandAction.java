package us.smartmc.core.itemcommands;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import us.smartmc.core.instance.player.SmartCorePlayer;

public class BungeeCommandAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        String name = label.split(" ")[0];
        label = label.replaceFirst(name + " ", "");
        SmartCorePlayer.get(clickHandler.player()).executeBungeeCordCommand("/" + label);
    }
}
