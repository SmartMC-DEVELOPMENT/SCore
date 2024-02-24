package us.smartmc.lobbycosmetics.cosmetic;

import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;

public abstract class Cosmetic implements ICosmetic {

    private final String id;

    public Cosmetic(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
