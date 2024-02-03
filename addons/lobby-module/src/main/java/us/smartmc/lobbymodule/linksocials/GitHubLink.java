package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class GitHubLink extends LinkSocialAction {

    public GitHubLink() {
        super(LinkSocialType.GITHUB);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "www.github.com/[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public String getValidExample() {
        return "www.github.com/{USER}";
    }
}
