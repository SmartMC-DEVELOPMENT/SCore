package us.smartmc.gamescore.instance.storage;

import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.instance.config.DataConfig;

public class SBackendData extends DataConfig {

    public SBackendData(String id) {
        super(id);
    }

    public void load() {
        BackendConnection.getBackendConnection().ifPresent(connection -> {
            connection.getConfig(getId()).join().getDataMap().forEach(this::set);
        });
    }

    public void save() {
        BackendConnection.getBackendConnection().ifPresent(connection -> {
            connection.saveConfig(this);
        });
    }

}
