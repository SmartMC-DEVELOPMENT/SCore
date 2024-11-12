package us.smartmc.lobbymodule.listener;

import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.PluginUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.menu.LobbiesMenu;

public class ConnectLobbyListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        GUIMenu guiMenu = GUIMenu.getOpenGUI(player);
        if (guiMenu == null) return;
        if (!(guiMenu instanceof LobbiesMenu lobbiesMenu)) return;
        int slot = event.getSlot();
        ItemStack item = lobbiesMenu.get(slot);
        if (item == null) return;

        Material type = item.getType();

        if (type.equals(Material.QUARTZ_BLOCK)) {
            int lobbyNumber = slot + 1;
            PluginUtils.redirectTo(player, "lobby" + lobbyNumber);
        }
    }
}
