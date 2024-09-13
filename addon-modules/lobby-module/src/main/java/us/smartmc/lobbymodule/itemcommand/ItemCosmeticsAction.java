package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;

public class ItemCosmeticsAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {
        Player player = handler.getPlayer();
        CorePlayer corePlayer = CorePlayer.get(player);

        if (!player.hasPermission("*")) {
            return;
        }
    }
}
