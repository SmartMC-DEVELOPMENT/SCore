package us.smartmc.npcsmodule.instance;

import java.util.Collection;
import java.util.HashMap;

public class ManagerRegistry<K, V> extends HashMap<K, V> {

    public void register(K key, V value) {
        if (containsKey(key)) return;
        put(key, value);
    }

    public void unregister(K key) {
        remove(key);
    }

    @Override
    public Collection<V> values() {
        return super.values();
    }
}
