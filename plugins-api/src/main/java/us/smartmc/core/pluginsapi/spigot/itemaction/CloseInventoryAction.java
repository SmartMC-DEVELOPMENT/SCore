package us.smartmc.core.pluginsapi.spigot.itemaction;

import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;

public class CloseInventoryAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        clickHandler.player().closeInventory();
    }
}
