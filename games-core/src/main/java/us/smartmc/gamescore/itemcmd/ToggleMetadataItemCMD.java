package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.menu.ManageMetadataRegionMenu;

public class ToggleMetadataItemCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.getPlayer();
        String regionName = args[0];
        String metadataId = args[1];
        boolean toggled = RegionsManager.getInstance().get(regionName).getDefaultConfig().toggleMetadata(metadataId);
        RegionsManager.getInstance().get(regionName).getDefaultConfig().saveMetadata();
        String message = toggled ? "&aAdded metadata!" : "&cRemoved metadata!";
        player.sendMessage(ChatUtil.parse(message));

        ManageMetadataRegionMenu menu = (ManageMetadataRegionMenu) GUIMenu.getOpenGUI(player);
        menu.setItems();
    }
}
