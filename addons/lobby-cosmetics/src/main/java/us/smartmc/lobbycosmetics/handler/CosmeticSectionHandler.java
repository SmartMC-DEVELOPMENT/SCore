package us.smartmc.lobbycosmetics.handler;

import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;

public class CosmeticSectionHandler extends AddonHandler<CosmeticActionType, CosmeticSection> {

    @Override
    public CosmeticSection getDefaultValue(CosmeticActionType type) {
        return new CosmeticSection(type);
    }
}
