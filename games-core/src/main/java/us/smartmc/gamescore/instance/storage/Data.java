package us.smartmc.gamescore.instance.storage;

import lombok.Getter;
import us.smartmc.gamescore.instance.storage.loader.IDataLoader;
import us.smartmc.gamescore.instance.storage.saver.IDataSaver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Getter
public abstract class Data<
        Loader extends IDataLoader<DataLoaderObject>,
        Saver extends IDataSaver,
        DataLoaderObject>

        implements IData<
        Loader,
        Saver,
        DataLoaderObject> {

    protected final Map<String, Object> data = new HashMap<>();

    @Override
    public void save() {
        getSaverInstance().save(data);
    }

    @Override
    public void save(boolean async) {
        getSaverInstance().save(async, data);
    }

    @Override
    public void load() {
        data.clear();
        Map<String, Object> configData = getLoaderInstance().load(getDataLoaderObject());
        if (configData != null) data.putAll(configData);
    }

    public void addAll(Map<String, Object> mapData) {
        mapData.forEach(this::set);
    }

    public void set(String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentMap = data;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            currentMap = (Map<String, Object>) currentMap.computeIfAbsent(part, k -> new HashMap<>());
        }

        currentMap.put(parts[parts.length - 1], value);
    }

    @Override
    public void remove(String key) {
        String[] parts = key.split("\\.");
        removeRecursive(data, parts, 0);
    }

    @Override
    public boolean removeRecursive(Map<String, Object> map, String[] parts, int index) {
        if (index >= parts.length - 1) {
            return map.remove(parts[index]) != null && map.isEmpty();
        }

        String part = parts[index];
        Map<String, Object> nextMap = (Map<String, Object>) map.get(part);
        if (nextMap == null) {
            return false;
        }

        boolean shouldRemove = removeRecursive(nextMap, parts, index + 1);
        if (shouldRemove) {
            map.remove(part);
            return map.isEmpty();
        }
        return false;
    }

    public Object get(String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentMap = data;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            currentMap = (Map<String, Object>) currentMap.get(part);
            if (currentMap == null) {
                return null;
            }
        }

        return currentMap.get(parts[parts.length - 1]);
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<? super Object> values() {
        return data.values();
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> consumer) {
        data.forEach(consumer);
    }

    @Override
    public void registerDefault(String key, Object value) {
        if (containsKey(key)) return;
        set(key, value);
    }

    public boolean containsKey(String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentMap = data;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            currentMap = (Map<String, Object>) currentMap.get(part);
            if (currentMap == null) {
                return false;
            }
        }

        return currentMap.containsKey(parts[parts.length - 1]);
    }
}
