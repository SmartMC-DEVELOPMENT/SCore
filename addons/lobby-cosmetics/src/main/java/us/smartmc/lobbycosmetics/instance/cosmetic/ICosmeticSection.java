package us.smartmc.lobbycosmetics.instance.cosmetic;

public interface ICosmeticSection {

    void register(ICosmetic cosmetic);
    void unregister(String id);

    ICosmetic get(String id);

    String getId();
}
