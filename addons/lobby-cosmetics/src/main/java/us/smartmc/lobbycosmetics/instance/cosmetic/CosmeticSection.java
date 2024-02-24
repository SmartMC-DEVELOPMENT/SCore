package us.smartmc.lobbycosmetics.instance.cosmetic;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class CosmeticSection implements ICosmeticSection {

    @Getter
    private final CosmeticActionType type;

    private final Map<String, ICosmetic> cosmetics = new HashMap<>();

    public CosmeticSection(CosmeticActionType type) {
        this.type = type;
    }

    @Override
    public void register(ICosmetic cosmetic) {
        cosmetics.put(cosmetic.getId(), cosmetic);
    }

    @Override
    public void unregister(String id) {
        cosmetics.remove(id);
    }

    @Override
    public ICosmetic get(String id) {
        ICosmetic cosmetic = cosmetics.get(id);
        if (cosmetic == null) {
            cosmetic =
        }
        return ;
    }

    @Override
    public String getId() {
        return name;
    }
}
