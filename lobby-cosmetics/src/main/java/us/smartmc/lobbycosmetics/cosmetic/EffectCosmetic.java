package us.smartmc.lobbycosmetics.cosmetic;

import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;

public abstract class EffectCosmetic extends Cosmetic implements IEffectCosmetic {

    public EffectCosmetic(String id) {
        super(id);
    }

    @Override
    public CosmeticType getType() {
        return CosmeticType.EFFECTS;
    }
}
