package us.smartmc.lobbycosmetics.handler;

import java.util.HashMap;
import java.util.Map;

public abstract class AddonHandler<K, V> implements IAddonHandler<K, V> {

    private final Map<K, V> map = new HashMap<>();

    public V unregister(K key) {
        return map.remove(key);
    }

    public V get(K key) {
        V value = map.get(key);
        if (value == null) {
            value = getDefaultValue(key);
            map.put(key, value);
        }
        return value;
    }

}
