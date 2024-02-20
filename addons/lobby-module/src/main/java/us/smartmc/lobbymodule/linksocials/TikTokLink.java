package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://www.tiktok.com/@{0}", atDisabled = false)
public class TikTokLink extends LinkSocialAction {

    public TikTokLink() {
        super(LinkSocialType.TIKTOK);
    }

    @Override
    public String getValidExample() {
        return "@smartmcnetwork";
    }

    @Override
    public String getFormattedURL(String username) {
        if (!username.startsWith("@")) username = "@" + username;
        return "https://www.tiktok.com/" + username;
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                LinkSocialAction.DEFAULT_USERNAME_REGEX,
                "https://tiktok.com/@([a-zA-Z0-9_.]{1,24})",
                "https://www.tiktok.com/@([a-zA-Z0-9_.]{1,24})",
                "tiktok.com/@([a-zA-Z0-9_.]{1,24})",
                "www.tiktok.com/@([a-zA-Z0-9_.]{1,24})"
        };
    }

}
