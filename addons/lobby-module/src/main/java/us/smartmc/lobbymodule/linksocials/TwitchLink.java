package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://www.twitch.tv/{0}")
public class TwitchLink extends LinkSocialAction {

    public TwitchLink() {
        super(LinkSocialType.TWITCH);
    }

    @Override
    public String getValidExample() {
        return "@imsergioh_";
    }

    @Override
    public String getFormattedURL(String username) {
        return "https://www.twitch.tv/" + username.replaceFirst("@", "");
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                LinkSocialAction.DEFAULT_USERNAME_REGEX,
                "https://www.twitch\\.tv/([a-zA-Z0-9_]{4,25})",
                "https://twitch\\.tv/([a-zA-Z0-9_]{4,25})",
                "twitch\\.tv/([a-zA-Z0-9_]{4,25})",
                "www.twitch\\.tv/([a-zA-Z0-9_]{4,25})"
        };
    }

}
