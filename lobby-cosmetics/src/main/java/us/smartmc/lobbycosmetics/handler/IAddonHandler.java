package us.smartmc.lobbycosmetics.handler;

public interface IAddonHandler<K, V> {

    V getDefaultValue(K key);
}
