package us.smartmc.core.pluginsapi.data.managment;

import java.util.HashMap;

public class FactoryManager<K, V extends FactoryValue> extends HashMap<K, V> {

    public void register(K key, V value) {
        if (containsKey(key)) return;
        put(key, value);
        value.load();
    }

    public void unregister(K key) {
        if (!containsKey(key)) return;
        get(key).unload();
        remove(key);
    }
}
