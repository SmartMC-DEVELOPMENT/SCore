
package us.smartmc.core.pluginsapi.spigot.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class CoreUpdatableMenu extends CoreMenu implements IUpdatableMenu {

    public CoreUpdatableMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    @Override
    public void updateAll() {
        for (HumanEntity entity : inventory.getViewers()) {
            if (!(entity instanceof Player)) continue;
            Player player = (Player) entity;
            Inventory inventory = player.getOpenInventory().getTopInventory();
            update(inventory, player);
        }
    }
}
