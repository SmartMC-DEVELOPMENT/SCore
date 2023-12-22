package us.smartmc.core.pluginsapi.spigot.instance;

import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;

public interface ItemActionExecutor {

    void execute(ClickHandler clickHandler, String label, String[] args);
}
