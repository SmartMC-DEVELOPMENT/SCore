package us.smartmc.lobbycosmetics.instance.cosmetic;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CosmeticSection<V extends ICosmetic> implements ICosmeticSection<V> {

    @Getter
    private final CosmeticSectionInfo info;

    private final Map<String, V> cosmetics = new HashMap<>();

    public CosmeticSection() {
        this.info = getClass().getDeclaredAnnotation(CosmeticSectionInfo.class);
    }

    public Collection<String> getNames() {
        return cosmetics.keySet();
    }

    @SafeVarargs
    @Override
    public final void register(V... cosmetics) {
        for (V cosmetic : cosmetics) {
            this.cosmetics.put(cosmetic.getId(), cosmetic);
        }
    }

    @Override
    public void unregister(String... ids) {
        for (String id : ids) {
            cosmetics.remove(id);
        }
    }

    @Override
    public V get(String id) {
        return cosmetics.get(id);
    }

    @Override
    public void forEach(Consumer<V> consumer) {
        cosmetics.values().forEach(consumer);
    }

    @Override
    public Collection<V> getCosmetics() {
        return cosmetics.values();
    }

    @Override
    public Material getIconMaterial() {
        return info.icon();
    }

    @Override
    public CosmeticType getId() {
        return info.type();
    }
}
