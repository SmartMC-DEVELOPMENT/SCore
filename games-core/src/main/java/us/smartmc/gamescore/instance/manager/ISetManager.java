package us.smartmc.gamescore.instance.manager;

import java.util.Set;

public interface ISetManager<T> extends Set<T> {

    default boolean register(T value) {
        return add(value);
    }
}
