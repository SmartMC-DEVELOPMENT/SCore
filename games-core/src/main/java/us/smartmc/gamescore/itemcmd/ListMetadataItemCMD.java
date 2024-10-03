package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.menu.ManageMetadataRegionMenu;

import java.util.Optional;

public class ListMetadataItemCMD implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.getPlayer();
        String regionName = args[0];

        Optional<? extends CuboidRegion> optionalCuboidRegion = RegionsManager.getInstance().getRegion(regionName);

        if (optionalCuboidRegion.isEmpty()) {
            player.sendMessage(ChatUtil.color("&cRegion not found!"));
            return;
        }

        CuboidRegion region = optionalCuboidRegion.get();
        player.closeInventory();
        StringBuilder builder = new StringBuilder("&aList of all metadata at " + regionName + ":");

        for (String metadataId : region.getDefaultConfig().getMetadata()) {
            builder.append("\n");
            builder.append("&7 -&b ").append(metadataId);
        }

        String message = builder.toString();
        player.sendMessage(ChatUtil.parse(message));
    }
}
