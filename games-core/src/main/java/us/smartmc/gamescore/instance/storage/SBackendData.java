package us.smartmc.gamescore.instance.storage;

import org.bson.Document;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.instance.config.DataConfig;
import us.smartmc.backend.instance.config.IDataConfig;

public class SBackendData extends Document {

    private final String id;

    public SBackendData(String id) {
        this.id = id;
    }

    public void registerDefault(String key, Object value) {
        if (containsKey(key)) return;
        put(key, value);
    }

    public void load() {
        BackendConnection.getBackendConnection().ifPresent(connection -> {
            this.putAll(connection.getConfig(id).join().getDataMap());
        });
    }


    public void save() {
        BackendConnection.getBackendConnection().ifPresent(connection -> {
            IDataConfig dataConfig = new DataConfig(id);
            forEach(dataConfig::set);
            connection.saveConfig(dataConfig);
        });
    }

}
