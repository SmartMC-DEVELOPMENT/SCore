package us.smartmc.backend.instance.handler;

public interface IMapHandler<K, V> {

    void remove(K key);
    boolean containsKey(K key);
    void put(K key, V value);
    V getOrCreate(K key, Object... initArgs);
    V get(K key);
    void register(K key);

    V getDefaultValue(K key, Object... initArgs);

}
