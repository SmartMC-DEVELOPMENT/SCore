package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialInfo;
import us.smartmc.lobbymodule.instance.LinkSocialType;

@LinkSocialInfo(linkFormat = "@{0}", atDisabled = false)
public class DiscordLink extends LinkSocialAction {

    public DiscordLink() {
        super(LinkSocialType.DISCORD);
    }

    @Override
    public String getValidExample() {
        return "@smartbot";
    }
}
