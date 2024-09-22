package us.smartmc.gamescore.instance.storage.saver;

import java.util.Map;

public interface IDataSaver {

    default void save(Map<String, Object> data) {
        save(false, data);
    }

    void save(boolean async, Map<String, Object> data);

}
