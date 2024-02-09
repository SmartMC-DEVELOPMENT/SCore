package us.smartmc.lobbymodule.linksocials;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "https://www.twitter.com/{0}")
public class TwitterLink extends LinkSocialAction {

    public TwitterLink() {
        super(LinkSocialType.TWITTER);
    }

    @Override
    public String getValidExample() {
        return "@SmartMC_Net";
    }
}
