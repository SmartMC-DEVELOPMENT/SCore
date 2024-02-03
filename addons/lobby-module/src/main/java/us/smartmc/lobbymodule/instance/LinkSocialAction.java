package us.smartmc.lobbymodule.instance;

import lombok.Getter;

@Getter
public abstract class LinkSocialAction implements ILinkSocial {

    protected final LinkSocialType type;

    public LinkSocialAction(LinkSocialType type) {
        this.type = type;
    }

}
