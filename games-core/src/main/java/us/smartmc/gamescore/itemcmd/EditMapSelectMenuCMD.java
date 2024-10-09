package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.menu.EditMapSelectionMenu;
import us.smartmc.gamescore.menu.ManageMetadataRegionMenu;

public class EditMapSelectMenuCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.getPlayer();
        EditMapSelectionMenu menu = (EditMapSelectionMenu) GUIMenu.getOpenGUI(player);
        if (menu == null) return;
        if (args[0].equalsIgnoreCase("next")) {
            menu.goToPage(menu.getPage() + 1);
        }
        if (args[0].equalsIgnoreCase("previous")) {
            menu.goToPage(menu.getPage() - 1);
        }
    }
}
