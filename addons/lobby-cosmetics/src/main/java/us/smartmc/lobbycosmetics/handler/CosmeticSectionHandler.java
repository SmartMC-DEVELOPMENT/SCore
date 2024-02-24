package us.smartmc.lobbycosmetics.handler;

import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;
import us.smartmc.lobbycosmetics.cosmeticsection.HatSection;
import us.smartmc.lobbycosmetics.cosmeticsection.UnknownSection;

import java.util.function.Supplier;

public class CosmeticSectionHandler extends AddonHandler<CosmeticActionType, CosmeticSection> {

    @Override
    public CosmeticSection getDefaultValue(CosmeticActionType type, Supplier<? extends CosmeticSection> supplier) {
        return supplier.get();
    }

    @Override
    public CosmeticSection getDefaultValue(CosmeticActionType key) {
        CosmeticSection section = null;
        switch (key) {
            case HAT -> section = new HatSection();
            default -> section = new UnknownSection();
        }
        return section;
    }
}
