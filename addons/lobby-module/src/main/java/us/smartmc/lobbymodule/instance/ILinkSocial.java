package us.smartmc.lobbymodule.instance;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import us.smartmc.lobbymodule.LobbyModule;

public interface ILinkSocial {

    default void perform(CorePlayer player, String input) {
        // Set @ at beginning if not a url/link
        if (!input.contains("/")) {
            if (!input.startsWith("@")) input = "@" + input;
        }
        if (!isValidInput(input)) {
            player.get().sendMessage(ChatUtil.parse(player.get(), "&c<lang.lobby.link_socials_invalid_input>"));
            return;
        }
        String username = input;
        if (input.contains("/")) username = getUsernameFromUrl(input);
        LobbyModule.getLinkSocialsManager().associate(player, username);
    }

    default boolean isValidInput(String input) {
        for (String pattern : getValidRegexPatterns()) {
            if (input.matches(pattern)) {
                return true;
            }
        }
        return false;
    }

    String getUsernameFromUrl(String url);
    String getFormattedURL(String username);

    String[] getValidRegexPatterns();
    String getValidExample();

    String getFormattedLink(String username);

}
