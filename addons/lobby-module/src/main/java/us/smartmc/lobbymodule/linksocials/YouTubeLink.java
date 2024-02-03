package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class YouTubeLink extends LinkSocialAction {

    public YouTubeLink() {
        super(LinkSocialType.YOUTUBE);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "https://www\\.youtube\\.com/channel/[a-zA-Z0-9_-]+",
                "https://www\\.youtube\\.com/@[a-zA-Z0-9_-]+"
        };
    }
}
