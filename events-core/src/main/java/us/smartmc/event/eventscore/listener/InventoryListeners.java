package us.smartmc.event.eventscore.listener;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.smartmc.event.eventscore.instance.ClickHandler;
import us.smartmc.event.eventscore.menu.EventCoreMenu;

public class InventoryListeners implements Listener {

    @EventHandler
    public void executeClickAction(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        CorePlayer corePlayer = CorePlayer.get(player);
        if (corePlayer == null) return;
        CoreMenu coreMenu = corePlayer.getCurrentMenuOpen();

        if (!(coreMenu instanceof EventCoreMenu)) return;
        EventCoreMenu eventCoreMenu = (EventCoreMenu) coreMenu;
        eventCoreMenu.clickAction(new ClickHandler(event));
    }

}
