package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@LinkSocialInfo(linkFormat = "@{0}", atDisabled = false)
public class DiscordLink extends LinkSocialAction {

    public DiscordLink() {
        super(LinkSocialType.DISCORD);
    }

    @Override
    public String getFormattedURL(String username) {
        if (username.startsWith("@")) {
            return "https://www.youtube.com/@" + username.substring(1);
        } else if (username.matches("[a-zA-Z0-9-_]{24}")) {
            return "https://www.youtube.com/channel/" + username;
        } else if (username.matches("[a-zA-Z0-9-_]{1,20}")) {
            return "https://www.youtube.com/c/" + username;
        }
        return null;
    }

    @Override
    public String getValidExample() {
        return "@smartbot";
    }
}
