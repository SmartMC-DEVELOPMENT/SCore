package us.smartmc.core.pluginsapi.spigot.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IUpdatableMenu extends ICoreMenu {

    void update(Inventory inventory, Player player);

    void updateAll();

}
