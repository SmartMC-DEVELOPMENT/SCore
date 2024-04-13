package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://github.com/{0}")
public class GitHubLink extends LinkSocialAction {

    public GitHubLink() {
        super(LinkSocialType.GITHUB);
    }

    @Override
    public String getValidExample() {
        return "@ImSergioh";
    }

    @Override
    public String getFormattedURL(String username) {
        return "https://www.github.com/" + username.replaceFirst("@", "");
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                LinkSocialAction.DEFAULT_USERNAME_REGEX,
                "https://github.com/([a-zA-Z0-9][a-zA-Z0-9-]{0,38})",
                "https://www.github.com/([a-zA-Z0-9][a-zA-Z0-9-]{0,38})",
                "github.com/([a-zA-Z0-9][a-zA-Z0-9-]{0,38})",
                "www.github.com/([a-zA-Z0-9][a-zA-Z0-9-]{0,38})"
        };
    }

}
