package us.smartmc.backend.instance.handler;

import java.util.HashMap;
import java.util.Map;

public abstract class MapHandler<K, V> implements IMapHandler<K, V> {

    private static final Map<String, MapHandler<?, ?>> handlers = new HashMap<>();

    public MapHandler() {
        handlers.put(getClass().getName(), this);
    }

    private final Map<K, V> registry = new HashMap<>();

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
    public V getOrCreate(K key) {
        V value = get(key);
        if (value != null) return get(key);
        value = getDefaultValue(key);
        registry.put(key, value);
        return value;
    }

    public V get(K key) {
        return registry.get(key);
    }

    public void register(K key) {
        if (registry.containsKey(key)) return;
        registry.put(key, getDefaultValue(key));
    }

    public static MapHandler<?, ?> getHandler(Class<? extends MapHandler<?, ?>> clazzType) {
        return handlers.get(clazzType.getName());
    }

}
