package us.smartmc.lobbycosmetics.cosmetic;

import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;

public abstract class EffectCosmetic extends Cosmetic implements IEffectCosmetic {

    public EffectCosmetic(String id) {
        super(id);
    }

    @Override
    public CosmeticActionType getType() {
        return CosmeticActionType.EFFECT;
    }
}
