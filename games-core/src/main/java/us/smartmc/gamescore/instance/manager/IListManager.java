package us.smartmc.gamescore.instance.manager;

import java.util.List;

public interface IListManager<T> extends List<T> {

    default boolean register(T value) {
        return add(value);
    }
}
