package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://www.youtube.com/@{0}", atDisabled = false)
public class YouTubeLink extends LinkSocialAction {

    public YouTubeLink() {
        super(LinkSocialType.YOUTUBE);
    }

    @Override
    public String getValidExample() {
        return "@smartmc_net";
    }

    @Override
    public String getFormattedURL(String username) {
        return "https://www.youtube.com/" + username;
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                LinkSocialAction.DEFAULT_USERNAME_REGEX,
                "https://www.youtube.com/@([a-zA-Z0-9]{1,20})",
                "https://www.youtube.com/user/[a-zA-Z0-9]{1,20}",
                "https://youtube.com/user/[a-zA-Z0-9]{1,20}",
                "youtube.com/user/[a-zA-Z0-9]{1,20}",
                "www.youtube.com/user/[a-zA-Z0-9]{1,20}",
                "https://www.youtube.com/channel/[a-zA-Z0-9-_]{24}",
                "https://youtube.com/channel/[a-zA-Z0-9-_]{24}",
                "youtube.com/channel/[a-zA-Z0-9-_]{24}",
                "https://www.youtube.com/c/[a-zA-Z0-9-_]{1,20}",
                "https://youtube.com/c/[a-zA-Z0-9-_]{1,20}",
                "youtube.com/c/[a-zA-Z0-9-_]{1,20}",
                "www.youtube.com/c/[a-zA-Z0-9-_]{1,20}"
        };
    }

}
