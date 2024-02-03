package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class TikTokLink extends LinkSocialAction {

    public TikTokLink() {
        super(LinkSocialType.TIKTOK);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "www\\.tiktok\\.com/@[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public String getValidExample() {
        return "www.tiktok.com/@{USER}";
    }
}
