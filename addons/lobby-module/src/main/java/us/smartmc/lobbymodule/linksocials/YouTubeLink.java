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
}
