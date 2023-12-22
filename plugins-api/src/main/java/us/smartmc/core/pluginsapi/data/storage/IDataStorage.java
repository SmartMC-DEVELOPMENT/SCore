
package us.smartmc.core.pluginsapi.data.storage;

public interface IDataStorage {

    void set(ISaveDataData data);
    Object get(String path);

    void delete(String path);

}
