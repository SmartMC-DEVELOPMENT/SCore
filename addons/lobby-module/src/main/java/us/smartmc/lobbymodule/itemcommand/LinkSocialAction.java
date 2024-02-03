package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.core.util.PluginUtils;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class LinkSocialAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();
        LobbyModule.getLinkSocialsManager().registerPendingLink(player, LinkSocialType.valueOf(args[0]));
    }
}
