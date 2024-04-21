package us.smartmc.backend.instance.handler;

import java.util.HashMap;
import java.util.Map;

public abstract class MapHandler<K, V> implements IMapHandler<K, V> {

    private final Map<K, V> registry = new HashMap<>();

    @Override
    public void remove(K key) {
        registry.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return registry.containsKey(key);
    }

    @Override
    public void put(K key, V value) {
        registry.put(key, value);
    }

    @Override
    public V getOrCreate(K key, Object... args) {
        V value = get(key);
        if (value != null) return value;
        value = getDefaultValue(key, args);
        registry.put(key, value);
        return value;
    }

    @Override
    public V get(K key) {
        return registry.get(key);
    }

    @Override
    public void register(K key) {
        if (registry.containsKey(key)) return;
        registry.put(key, getDefaultValue(key));
    }
}
