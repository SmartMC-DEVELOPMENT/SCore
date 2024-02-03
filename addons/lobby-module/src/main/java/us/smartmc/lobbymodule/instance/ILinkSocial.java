package us.smartmc.lobbymodule.instance;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import us.smartmc.lobbymodule.LobbyModule;

public interface ILinkSocial {

    default void perform(CorePlayer player, String message) {
        if (!isValidURL(message)) {
            player.get().sendMessage(ChatUtil.parse(player.get(), "&c<lang.lobby.link_socials_invalid_url>"));
            return;
        }
        LobbyModule.getLinkSocialsManager().associate(player, message);
    }

    default boolean isValidURL(String url) {
        if (url.startsWith("https://")) url = url.replaceFirst("https://", "");
        for (String pattern : getValidRegexPatterns()) {
            if (url.matches(pattern)) {
                return true;
            }
        }
        return false;
    }

    String[] getValidRegexPatterns();
    String getValidExample();

}
