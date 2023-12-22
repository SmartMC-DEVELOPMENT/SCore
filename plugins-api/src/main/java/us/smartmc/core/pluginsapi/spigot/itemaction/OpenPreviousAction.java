package us.smartmc.core.pluginsapi.spigot.itemaction;

import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;

public class OpenPreviousAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        CorePlayer corePlayer = CorePlayer.get(clickHandler.player());
        corePlayer.openPreviousMenu();
    }
}
