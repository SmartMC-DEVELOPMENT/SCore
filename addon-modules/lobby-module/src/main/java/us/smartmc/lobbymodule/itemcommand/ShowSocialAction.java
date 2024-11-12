package us.smartmc.lobbymodule.itemcommand;

import me.imsergioh.pluginsapi.instance.ClickableComponent;
import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;

import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.instance.LinkSocialType;
import us.smartmc.lobbymodule.menu.LinkSocialsMenu;

public class ShowSocialAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String s, String[] args) {
        Player clicker = handler.clicker();
        CorePlayer clickerCorePlayer = CorePlayer.get(clicker);
        LinkSocialType type = LinkSocialType.valueOf(args[0]);
        String username = args[1];

        // Not linked account found:
        if (username.contains("✘")) {
            clickerCorePlayer.sendLanguageMessage("lobby", "linkSocials.noLinked");
            return;
        }
        String targetName = username;
        if (GUIMenu.getOpenGUI(handler.player()) instanceof LinkSocialsMenu menu) {
            targetName = menu.getTargetName();
        }

        String message = ChatUtil.parse(clicker, "<lang.lobby.linkSocials.showMessage>", targetName, type.getDisplayName());
        ClickableComponent component = new ClickableComponent();
        component.addURL(message, LobbyModule.getLinkSocialsManager().get(type).getFormattedURL(username));
        component.send(clicker);

        clicker.closeInventory();
    }
}
