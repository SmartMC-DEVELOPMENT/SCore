package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import us.smartmc.core.util.PluginUtils;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class LinkSocialAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();
        LinkSocialType type = LinkSocialType.valueOf(args[0]);
        if (clickHandler.clickEvent().isRightClick()) {
            LobbyModule.getLinkSocialsManager().removeCurrentLink(CorePlayer.get(player), type);
            return;
        }
        LobbyModule.getLinkSocialsManager().registerPendingLink(player, type);
    }
}
