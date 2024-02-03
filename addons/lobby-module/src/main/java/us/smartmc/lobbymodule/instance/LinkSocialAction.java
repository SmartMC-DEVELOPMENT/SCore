package us.smartmc.lobbymodule.instance;

import lombok.Getter;

public abstract class LinkSocialAction implements ILinkSocial {

    @Getter
    protected final LinkSocialType type;

    public LinkSocialAction(LinkSocialType type) {
        this.type = type;
    }

}
