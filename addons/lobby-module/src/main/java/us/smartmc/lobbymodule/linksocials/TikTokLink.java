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
}
