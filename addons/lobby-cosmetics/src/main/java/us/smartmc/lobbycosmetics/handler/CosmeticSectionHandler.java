package us.smartmc.lobbycosmetics.handler;

import us.smartmc.lobbycosmetics.cosmeticsection.EffectSection;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;
import us.smartmc.lobbycosmetics.cosmeticsection.HatSection;
import us.smartmc.lobbycosmetics.cosmeticsection.UnknownSection;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;

public class CosmeticSectionHandler extends AddonHandler<CosmeticType, CosmeticSection<?>> {

    @Override
    public CosmeticSection<?> getDefaultValue(CosmeticType type) {
        CosmeticSection<?> section;
        switch (type) {
            case HATS -> section = new HatSection();
            case EFFECTS -> section = new EffectSection();
            default -> section = new UnknownSection();
        }
        CosmeticsSectionMessages.registerMessages(section);
        return section;
    }
}
