package us.smartmc.gamescore.instance.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class MapManager<K, V> implements IMapManager<K, V> {

    private static final Map<Class<? extends MapManager<?, ?>>, MapManager<?, ?>> registry = new HashMap<>();

    private final Map<K, V> map = new HashMap<>();

    protected MapManager() {
    }

    public static <T extends MapManager<?, ?>> T getManager(final Class<T> clazz, Object... initArgs) {
        MapManager<?, ?> manager = registry.get(clazz);
        if (manager == null) {
            try {
                manager = (MapManager<?, ?>) createManager(clazz, initArgs);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        if (clazz.isInstance(manager)) {
            return clazz.cast(manager);
        }
        return null;
    }

    private static Object createManager(final Class<? extends MapManager<?, ?>> clazz, Object... initArgs) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        MapManager<?, ?> o = (MapManager<?, ?>) constructor.newInstance(initArgs);
        constructor.setAccessible(false);
        registry.put(clazz, o);
        return o;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
