package us.smartmc.lobbycosmetics.instance.cosmetic;

public abstract class Cosmetic implements ICosmetic {

    private final String name;

    public Cosmetic(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

}
