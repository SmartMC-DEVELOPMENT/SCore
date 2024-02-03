package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class TwitchLink extends LinkSocialAction {

    public TwitchLink() {
        super(LinkSocialType.TWITCH);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "www\\.twitch\\.tv/[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public String getValidExample() {
        return "www.twitch.tv/{USER}";
    }
}
