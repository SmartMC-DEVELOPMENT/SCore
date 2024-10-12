package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.manager.map.EditMapSessionsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;
import us.smartmc.gamescore.menu.ManageMetadataRegionMenu;

public class StartEditSessionCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.getPlayer();
        String mapName = args[0];
        EditMapSessionsManager manager = MapManager.getManager(EditMapSessionsManager.class);
        if (manager == null) return;
        player.closeInventory();
        manager.createSessionByName(player, mapName);
        new EditMapInventoryMenu(player, mapName).set(player);
        player.sendMessage(ChatUtil.color("&aYou are now editing map of " + mapName));
    }
}
