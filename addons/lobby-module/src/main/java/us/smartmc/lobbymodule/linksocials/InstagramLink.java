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
}
