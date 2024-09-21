package us.smartmc.gamescore.instance.manager;

import java.util.Map;

public interface IMapManager<K, V> extends Map<K, V> {

    default V register(K key) {
        return getOrCreate(key);
    }

    default V getOrCreate(K key) {
        V value = get(key);
        if (value != null) return value;
        value = createValueByKey(key);
        put(key, value);
        return value;
    }

    V createValueByKey(K key);

}
