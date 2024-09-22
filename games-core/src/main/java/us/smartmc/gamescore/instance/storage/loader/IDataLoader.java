package us.smartmc.gamescore.instance.storage.loader;

import java.util.Map;

public interface IDataLoader<T> {

    Map<String, Object> load(T dataObject);

}
