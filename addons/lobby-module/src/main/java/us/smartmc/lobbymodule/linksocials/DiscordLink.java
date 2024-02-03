package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class DiscordLink extends LinkSocialAction {

    public DiscordLink() {
        super(LinkSocialType.DISCORD);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "@[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public String getValidExample() {
        return "@{USER}";
    }
}
