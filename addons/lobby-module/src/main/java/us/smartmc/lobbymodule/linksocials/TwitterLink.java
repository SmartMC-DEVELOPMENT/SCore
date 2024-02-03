package us.smartmc.lobbymodule.linksocials;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbymodule.instance.LinkSocialAction;
import us.smartmc.lobbymodule.instance.LinkSocialType;

public class TwitterLink extends LinkSocialAction {

    public TwitterLink() {
        super(LinkSocialType.TWITTER);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "\\twitter\\.com/[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public void perform(CorePlayer player, String message) {
        if (message.startsWith("www.")) message = message.replaceFirst("www.", "");
        super.perform(player, message);
    }

    @Override
    public boolean isValidURL(String url) {
        if (url.startsWith("https://www.")) url = url.replaceFirst("https://www.", "");
        return super.isValidURL(url);
    }

    @Override
    public String getValidExample() {
        return "www.twitter.com/{USER}";
    }
}
