package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://www.twitch.tv/{0}")
public class TwitchLink extends LinkSocialAction {

    public TwitchLink() {
        super(LinkSocialType.TWITCH);
    }

    @Override
    public String getValidExample() {
        return "@imsergioh_";
    }
}
