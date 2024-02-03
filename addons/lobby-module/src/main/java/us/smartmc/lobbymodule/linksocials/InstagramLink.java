package us.smartmc.lobbymodule.linksocials;

import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class InstagramLink extends LinkSocialAction {

    public InstagramLink() {
        super(LinkSocialType.INSTAGRAM);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "www\\.instagram\\.com/[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public String getValidExample() {
        return "www.instagram.com/{USER}";
    }
}
