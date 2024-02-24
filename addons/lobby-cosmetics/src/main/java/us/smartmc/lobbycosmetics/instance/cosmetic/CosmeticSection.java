package us.smartmc.lobbycosmetics.instance.cosmetic;

import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public abstract class CosmeticSection<V extends ICosmetic> implements ICosmeticSection<V> {

    @Getter
    private final CosmeticSectionInfo info;

    private final Map<String, V> cosmetics = new HashMap<>();

    public CosmeticSection() {
        info = getClass().getDeclaredAnnotation(CosmeticSectionInfo.class);
    }

    @Override
    public void register(V cosmetic) {
        cosmetics.put(cosmetic.getId(), cosmetic);
    }

    @Override
    public void unregister(String id) {
        cosmetics.remove(id);
    }

    @Override
    public V get(String id) {
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
