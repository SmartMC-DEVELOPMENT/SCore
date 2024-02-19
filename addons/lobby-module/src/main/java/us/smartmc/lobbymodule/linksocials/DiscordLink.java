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
        return "https://www.discord.gg/users/" + username;
    }

    @Override
    public String getValidExample() {
        return "@smartbot";
    }
}
