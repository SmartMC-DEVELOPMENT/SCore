package us.smartmc.core.pluginsapi.spigot.itemaction;

import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;

public class CommandAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        String name = label.split(" ")[0];
        label = label.replaceFirst(name + " ", "");
        clickHandler.player().performCommand(label);
    }
}
