package us.smartmc.lobbymodule.linksocials;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://www.twitter.com/{0}")
public class TwitterLink extends LinkSocialAction {

    public TwitterLink() {
        super(LinkSocialType.TWITTER);
    }

    @Override
    public String getValidExample() {
        return "@SmartMC_Net";
    }

    @Override
    public String getFormattedURL(String username) {
        return "https://twitter.com/" + username.replaceFirst("@", "");
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                LinkSocialAction.DEFAULT_USERNAME_REGEX,
                "https://twitter\\.com/([a-zA-Z0-9_]{1,15})",
                "https://www.twitter\\.com/([a-zA-Z0-9_]{1,15})",
                "twitter\\.com/([a-zA-Z0-9_]{1,15})",
                "www.twitter\\.com/([a-zA-Z0-9_]{1,15})"
        };
    }

}
