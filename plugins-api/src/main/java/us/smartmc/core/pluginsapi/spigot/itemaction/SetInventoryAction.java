package us.smartmc.core.pluginsapi.spigot.itemaction;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.handler.MenusManager;
import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;

public class SetInventoryAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.getPlayer();
        String name = args[0];
        MenusManager.loadForPlayer(player, name).set(player);
    }
}
