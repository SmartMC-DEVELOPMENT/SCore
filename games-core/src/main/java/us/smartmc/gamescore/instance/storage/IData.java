package us.smartmc.gamescore.instance.storage;

import us.smartmc.gamescore.instance.storage.loader.IDataLoader;
import us.smartmc.gamescore.instance.storage.saver.IDataSaver;

import java.util.*;
import java.util.function.BiConsumer;

public interface IData<
        Loader extends IDataLoader<DataLoaderObject>,
        Saver extends IDataSaver,
        DataLoaderObject> {

    Collection<? super Object> values();

    Set<String> keySet();

    void forEach(BiConsumer<? super String, ? super Object> consumer);

    void registerDefault(String key, Object value);

    void set(String key, Object value);

    void remove(String key);

    boolean removeRecursive(Map<String, Object> map, String[] parts, int index);

    Object get(String key);

    boolean containsKey(String key);

    void load();

    void save(boolean async);

    default void save() {
        save(true);
    }

    default String getString(String key) {
        return get(key, String.class);
    }

    default <T> List<T> getList(String key, Class<T> type) {
        List<T> list = new ArrayList<>();
        for (Object o : get(key, List.class)) {
            if (o != null && type.isAssignableFrom(o.getClass())) {
                list.add(type.cast(o));
            }
        }
        return list;
    }

    default int getInt(String key) {
        return get(key, Number.class).intValue();
    }

    default double getDouble(String key) {
        return get(key, Number.class).doubleValue();
    }

    default boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }

    default <T> T get(String key, Class<T> type) {
        Object o = get(key);
        if (o != null && type.isAssignableFrom(o.getClass())) {
            return type.cast(o);
        }
        return null;
    }

    String getName();

    DataLoaderObject getDataLoaderObject();

    Saver getSaverInstance();

    Loader getLoaderInstance();
}
