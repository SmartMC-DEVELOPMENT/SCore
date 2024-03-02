package us.smartmc.event.eventscore.listener;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.event.eventscore.menu.EventCoreMenu;

public class CorePlayerListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void executeClickAction(PlayerJoinEvent event) {
        CorePlayer corePlayer = new CorePlayer(event.getPlayer().getUniqueId());
        event.getPlayer().sendMessage("CorePlayer=" + corePlayer);
    }

}
