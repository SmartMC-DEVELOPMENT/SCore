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
}
