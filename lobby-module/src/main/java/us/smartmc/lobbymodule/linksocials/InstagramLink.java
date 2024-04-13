package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "@{0}")
public class InstagramLink extends LinkSocialAction {

    public InstagramLink() {
        super(LinkSocialType.INSTAGRAM);
    }

    @Override
    public String getValidExample() {
        return "@minecraft";
    }

    @Override
    public String getFormattedURL(String username) {
        return "https://www.instagram.com/"+username.replaceFirst("@", "");
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                LinkSocialAction.DEFAULT_USERNAME_REGEX,
                "https://instagram.com/([a-zA-Z0-9_.]{1,30})",
                "https://instagram.com/([a-zA-Z0-9_.]{1,30})/",
                "https://www.instagram.com/([a-zA-Z0-9_.]{1,30})",
                "https://www.instagram.com/([a-zA-Z0-9_.]{1,30})/",
                "instagram.com/([a-zA-Z0-9_.]{1,30})",
                "instagram.com/([a-zA-Z0-9_.]{1,30})/",
                "www.instagram.com/([a-zA-Z0-9_.]{1,30})",
                "www.instagram.com/([a-zA-Z0-9_.]{1,30})/"
        };
    }
}
