package us.smartmc.lobbycosmetics.instance.cosmetic;

import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public abstract class CosmeticSection implements ICosmeticSection {

    @Getter
    private final CosmeticSectionInfo info;

    private final Map<String, ICosmetic> cosmetics = new HashMap<>();

    public CosmeticSection() {
        info = getClass().getDeclaredAnnotation(CosmeticSectionInfo.class);
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
        return cosmetics.get(id);
    }

    @Override
    public Material getIconMaterial() {
        return info.icon();
    }

    @Override
    public CosmeticActionType getId() {
        return info.type();
    }
}
