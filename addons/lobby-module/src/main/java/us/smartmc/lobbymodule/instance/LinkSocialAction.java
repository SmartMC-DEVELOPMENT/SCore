package us.smartmc.lobbymodule.instance;

import lombok.Getter;

@Getter
public abstract class LinkSocialAction implements ILinkSocial {

    private final LinkSocialInfo info;
    private final LinkSocialType type;

    public LinkSocialAction(LinkSocialType type) {
        this.type = type;
        this.info = getClass().getDeclaredAnnotation(LinkSocialInfo.class);
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                "@[a-zA-Z0-9_-]+"
        };
    }

    @Override
    public String getFormattedLink(String username) {
        return String.format(info.linkFormat(), username);
    }
}
